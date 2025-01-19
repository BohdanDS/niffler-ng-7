package qa.guru.niffler.data.dao.impl;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {


    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }


    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
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
    public Optional<AuthUserEntity> findUserById(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
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
    public List<AuthUserEntity> findAll() {

        List<AuthUserEntity> authUserEntities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
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

    @Override
    public void deleteUser(UUID id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?")) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
