package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.Databases;
import qa.guru.niffler.data.dao.CategoryDao;
import qa.guru.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public CategoryEntity create(CategoryEntity categoryEntity) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived)"
                            + "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, categoryEntity.getUsername());
                preparedStatement.setString(2, categoryEntity.getName());
                preparedStatement.setBoolean(3, categoryEntity.isArchived());

                preparedStatement.executeUpdate();

                final UUID generatedKey;
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can't find Id in ResultSet");
                    }
                    categoryEntity.setId(generatedKey);
                    return categoryEntity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM category WHERE id = ?")) {
                preparedStatement.setObject(1, id);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        CategoryEntity categoryEntity = new CategoryEntity();
                        categoryEntity.setId(resultSet.getObject("id", UUID.class));
                        categoryEntity.setUsername(resultSet.getString("username"));
                        categoryEntity.setName(resultSet.getString("name"));
                        categoryEntity.setArchived(resultSet.getBoolean("archived"));
                        return Optional.of(categoryEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
