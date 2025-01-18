package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.entity.auth.AuthAuthorityEntity;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    private final DataSource dataSource;

    public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void createAuthority(AuthAuthorityEntity... authAuthorityEntities) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
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
    public List<AuthAuthorityEntity> getAuthorityByUserId(UUID id) {
        return null;
    }

    @Override
    public void deleteUserAuthority(UUID id) throws SQLException {

    }
}
