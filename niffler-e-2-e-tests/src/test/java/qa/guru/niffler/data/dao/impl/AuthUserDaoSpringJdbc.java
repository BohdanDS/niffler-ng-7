package qa.guru.niffler.data.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.mapper.AuthUserEntityRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final DataSource dataSource;

    public AuthUserDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO \"user\" (username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " + "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setObject(2, pe.encode(authUserEntity.getPassword()));
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
    public Optional<AuthUserEntity> findUserById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable
                (jdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE id = ?",
                AuthUserEntityRowMapper.instance, id));
    }

    @Override
    public void deleteUser(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", id);
    }
}


