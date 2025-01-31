package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.SpendDao;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.mapper.SpendEntityRowMapper;
import qa.guru.niffler.data.tpl.DataSources;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES(?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, spendEntity.getUsername());
            preparedStatement.setObject(2, spendEntity.getSpendDate());
            preparedStatement.setString(3, spendEntity.getCurrency().name());
            preparedStatement.setDouble(4, spendEntity.getAmount());
            preparedStatement.setString(5, spendEntity.getDescription());
            preparedStatement.setObject(6, spendEntity.getCategory().getId());

            return preparedStatement;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        spendEntity.setId(generatedKey);
        return spendEntity;
    }

    @Override
    public Optional<SpendEntity> findEntityById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                        "SELECT * FROM \"spend\" WHERE id = ?",
                        SpendEntityRowMapper.instance, id));
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"spend\" WHERE username = ?",
                SpendEntityRowMapper.instance, username);
    }

    @Override
    public List<SpendEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM spend ",
                SpendEntityRowMapper.instance);
    }

    @Override
    public void deleteSpend(SpendEntity spendEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update("DELETE FROM \"spend\" WHERE id = ?", spendEntity.getId());
    }
}
