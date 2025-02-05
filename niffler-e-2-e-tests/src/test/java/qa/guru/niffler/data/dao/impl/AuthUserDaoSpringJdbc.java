package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.mapper.AuthUserEntityRowMapper;
import qa.guru.niffler.data.tpl.DataSources;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO \"user\" (\"username\", \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                            + "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setObject(2, authUserEntity.getPassword());
            preparedStatement.setBoolean(3, authUserEntity.getEnabled());
            preparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            preparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            preparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            return preparedStatement;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        authUserEntity.setId(generatedKey);

        return authUserEntity;
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "UPDATE \"user\" SET " +
                            "username = ?, " +
                            "password = ?, " +
                            "enabled = ?, " +
                            "account_non_expired = ?, " +
                            "account_non_locked = ?, " +
                            "credentials_non_expired = ? " +
                            "WHERE id = ?"
            );
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setString(2, authUserEntity.getPassword());
            preparedStatement.setBoolean(3, authUserEntity.getEnabled());
            preparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            preparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            preparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());
            preparedStatement.setObject(7, authUserEntity.getId());
            return preparedStatement;
        });
        return authUserEntity;
    }

    @Override
    public Optional<AuthUserEntity> findUserById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        AuthUserEntityRowMapper.instance, id));
    }

    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE username = ?",
                        AuthUserEntityRowMapper.instance, username));
    }

    @Override
    public void removeUser(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?"
            );
            preparedStatement.setObject(1, authUserEntity.getId());
            preparedStatement.execute();
            return preparedStatement;
        });
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\" ",
                AuthUserEntityRowMapper.instance);
    }

}


