package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.Databases;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.entity.spend.SpendEntity;

import java.sql.*;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity create(SpendEntity spendEntity) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
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

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
