package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;

import qa.guru.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.*;

import static qa.guru.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                        "VALUES(?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setObject(2, spendEntity.getSpendDate());
            preparedStatement.setString(3, spendEntity.getCurrency().name());
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, spendEntity.getCategory().getId());

            preparedStatement.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find Id in ResultSet");
                }
                spendEntity.setId(generatedKey);
                return spendEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findEntityById(UUID id) {
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    SpendEntity spendEntity = new SpendEntity();
                    CategoryEntity categoryEntity = new CategoryEntity();

                    categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
                    spendEntity.setId(resultSet.getObject("id", UUID.class));
                    spendEntity.setUsername(resultSet.getString("username"));
                    spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                    spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    spendEntity.setAmount(resultSet.getDouble("amount"));
                    spendEntity.setDescription(resultSet.getString("description"));
                    spendEntity.setCategory(categoryEntity);


                    return Optional.of(spendEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {

        List<SpendEntity> spendEntities = new ArrayList<>();
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ?")) {
            preparedStatement.setObject(1, username);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {

                    SpendEntity spendEntity = new SpendEntity();
                    CategoryEntity categoryEntity = new CategoryEntity();

                    spendEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
                    spendEntity.setUsername(resultSet.getString("username"));
                    spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                    spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    spendEntity.setAmount(resultSet.getDouble("amount"));
                    spendEntity.setDescription(resultSet.getString("description"));
                    spendEntity.setCategory(categoryEntity);

                    spendEntities.add(spendEntity);
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAll() {
        List<SpendEntity> spendEntities = new ArrayList<>();
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE")) {
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {

                    SpendEntity spendEntity = new SpendEntity();
                    CategoryEntity categoryEntity = new CategoryEntity();

                    spendEntity.setId(resultSet.getObject("id", UUID.class));
                    categoryEntity.setId(resultSet.getObject("category_id", UUID.class));
                    spendEntity.setUsername(resultSet.getString("username"));
                    spendEntity.setSpendDate(resultSet.getDate("spend_date"));
                    spendEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    spendEntity.setAmount(resultSet.getDouble("amount"));
                    spendEntity.setDescription(resultSet.getString("description"));
                    spendEntity.setCategory(categoryEntity);

                    spendEntities.add(spendEntity);
                }
                return spendEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spendEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE id = ?")) {
            preparedStatement.setObject(1, spendEntity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
