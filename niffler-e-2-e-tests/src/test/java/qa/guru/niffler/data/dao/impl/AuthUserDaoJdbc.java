package qa.guru.niffler.data.dao.impl;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public class AuthUserDaoJdbc implements AuthUserDao {


    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static final Config CFG = Config.getInstance();

    @Override
    public @Nullable AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, \"password\", enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setObject(2, pe.encode(authUserEntity.getPassword()));
            preparedStatement.setBoolean(3, authUserEntity.getEnabled());
            preparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            preparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            preparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());

            preparedStatement.executeUpdate();

            final UUID generatedKey;

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find Id in ResultSet");
                }
                authUserEntity.setId(generatedKey);
                return authUserEntity;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nonnull AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "UPDATE \"user\" SET username = ?, " +
                        "password = ?, " +
                        "enabled = ?, " +
                        "account_non_expired = ?, " +
                        "account_non_locked = ?, " +
                        "credentials_non_expired = ? " +
                        "WHERE id = ?"
        )){
            preparedStatement.setString(1, authUserEntity.getUsername());
            preparedStatement.setString(2, authUserEntity.getPassword());
            preparedStatement.setBoolean(3, authUserEntity.getEnabled());
            preparedStatement.setBoolean(4, authUserEntity.getAccountNonExpired());
            preparedStatement.setBoolean(5, authUserEntity.getAccountNonLocked());
            preparedStatement.setBoolean(6, authUserEntity.getCredentialsNonExpired());
            preparedStatement.setObject(7, authUserEntity.getId());

            preparedStatement.executeUpdate();
            return authUserEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserById(UUID id) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserByUsername(String username) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            preparedStatement.setObject(1, username);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                if (resultSet.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )){
            preparedStatement.setObject(1, authUserEntity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nonnull List<AuthUserEntity> findAll() {

        List<AuthUserEntity> authUserEntities = new ArrayList<>();

        try (PreparedStatement preparedStatement = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();

                    authUserEntity.setId(resultSet.getObject("id", UUID.class));
                    authUserEntity.setUsername(resultSet.getString("username"));
                    authUserEntity.setPassword(resultSet.getString("password"));
                    authUserEntity.setEnabled(resultSet.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));

                    authUserEntities.add(authUserEntity);
                }
                return authUserEntities;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
