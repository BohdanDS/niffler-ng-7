package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.data.mapper.SpendEntityRowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.tpl.Connections.holder;
@ParametersAreNonnullByDefault
public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public @Nonnull SpendEntity createSpend(SpendEntity spendEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                        "VALUES(?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setDate(2, new java.sql.Date(spendEntity.getSpendDate().getTime()));
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
    public @Nonnull Optional<SpendEntity> findSpendById(UUID id) {
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
    public @Nonnull List<SpendEntity> findAllByUsername(String username) {

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
    public @Nonnull SpendEntity updateSpend(SpendEntity spendEntity) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "UPDATE spend SET " +
                        "username = ?, " +
                        "spend_date = ?, " +
                        "currency = ?, " +
                        "amount = ?, " +
                        "description = ?, " +
                        "category_id = ?"
        )) {
            ps.setString(1, spendEntity.getUsername());
            ps.setDate(2, new java.sql.Date(spendEntity.getSpendDate().getTime()));
            ps.setString(3, spendEntity.getCurrency().name());
            ps.setDouble(4, spendEntity.getAmount());
            ps.setString(5, spendEntity.getDescription());
            ps.setObject(6, spendEntity.getCategory().getId());
            ps.executeUpdate();

            return spendEntity;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nonnull List<SpendEntity> findAll() {
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
    public @Nonnull Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ? AND description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity se =
                            SpendEntityRowMapper.instance.mapRow(rs, rs.getRow());
                    return Optional.ofNullable(se);
                } else {
                    return Optional.empty();
                }
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
