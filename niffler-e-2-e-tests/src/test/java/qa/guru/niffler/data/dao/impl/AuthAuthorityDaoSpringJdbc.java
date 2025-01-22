package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.tpl.DataSources;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;



public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void createAuthority(AuthorityEntity... authAuthorityEntities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO \"authority\" (user_id, authority) VALUES(?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
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
    public List<AuthorityEntity> getAuthorityByUserId(UUID id) {
        return null;
    }

    @Override
    public void deleteUserAuthority(UUID id) throws SQLException {

    }
}
