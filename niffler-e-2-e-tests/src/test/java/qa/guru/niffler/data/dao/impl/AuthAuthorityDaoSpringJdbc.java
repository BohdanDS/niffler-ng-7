package qa.guru.niffler.data.dao.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.mapper.AuthAutorityEntityRowMapper;
import qa.guru.niffler.data.tpl.DataSources;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authAuthorityEntities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO \"authority\" (user_id, authority) VALUES(?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authAuthorityEntities[i].getUser().getId());
                        ps.setString(2, authAuthorityEntities[i].getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authAuthorityEntities.length;
                    }
                }
        );
    }

    @Override
    public void update(AuthorityEntity... authAuthorityEntities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "UPDATE \"authority\" SET user_id = ?, authority = ? WHERE id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authAuthorityEntities[i].getUser().getId());
                        ps.setObject(1, authAuthorityEntities[i].getAuthority().name());
                        ps.setObject(3, authAuthorityEntities[i].getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return authAuthorityEntities.length;
                    }
                }
        );
    }

    @Override
    public List<AuthorityEntity> getAuthorityByUserId(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"authority\" WHERE user_id = ?");
                    ps.setObject(1, id); // Устанавливаем значение параметра
                    return ps;
                },
                AuthAutorityEntityRowMapper.instance
        );

    }

    @Override
    public void deleteUserAuthority(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(
                "DELETE FROM \"authority\" WHERE user_id = ?",
                id
        );
    }
}
