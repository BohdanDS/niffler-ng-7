package qa.guru.niffler.data.repository.impl;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.Authority;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.mapper.AuthUserEntityRowMapper;
import qa.guru.niffler.data.repository.AuthUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.tpl.Connections.holder;

public class AuthUserRepositoryJdbc implements AuthUserRepository {


    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement userPreparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        ); PreparedStatement autorityPreparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority)" +
                        "VALUES(?, ?)"
        )) {
            userPreparedStatement.setString(1, authUserEntity.getUsername());
            userPreparedStatement.setObject(2, pe.encode(authUserEntity.getPassword()));
            userPreparedStatement.setBoolean(3, authUserEntity.getEnabled());
            userPreparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            userPreparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            userPreparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            userPreparedStatement.executeUpdate();

            final UUID generatedKey;

            try (ResultSet resultSet = userPreparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find Id in ResultSet");
                }
                authUserEntity.setId(generatedKey);

                for (AuthorityEntity authorityEntity : authUserEntity.getAuthorities()) {
                    autorityPreparedStatement.setObject(1, generatedKey);
                    autorityPreparedStatement.setString(2, authorityEntity.getAuthority().name());
                    autorityPreparedStatement.addBatch();
                    autorityPreparedStatement.clearParameters();
                }

                autorityPreparedStatement.executeBatch();

                return authUserEntity;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        return null;
    }

    @Override
    public Optional<AuthUserEntity> findUserById(UUID id) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u join authority a on u.id = a.user_id WHERE u.id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();

            List<AuthorityEntity> authorityEntities = new ArrayList<>();

            AuthUserEntity user = null;
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    if (user == null) {
                        user = AuthUserEntityRowMapper.instance.mapRow(resultSet, 1);
                    }

                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setUser(user);
                    authorityEntity.setId(resultSet.getObject("a.id", UUID.class));
                    authorityEntity.setAuthority(Authority.valueOf(resultSet.getString("authority")));
                    authorityEntities.add(authorityEntity);

                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setAuthorities(authorityEntities);
                    return Optional.of(user);
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void removeUser(AuthUserEntity authUserEntity) {

    }
}
