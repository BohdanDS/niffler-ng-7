package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.Databases;
import qa.guru.niffler.data.dao.UserDataUserDao;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDataUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (Connection connection = Databases.connection(CFG.userDataJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name)"
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, userEntity.getUsername());
                preparedStatement.setString(2, userEntity.getCurrency().name());
                preparedStatement.setString(3, userEntity.getFirstname());
                preparedStatement.setObject(4, userEntity.getSurname());
                preparedStatement.setBytes(5, userEntity.getPhoto());
                preparedStatement.setBytes(6, userEntity.getPhotoSmall());
                preparedStatement.setString(7, userEntity.getFullname());

                preparedStatement.executeUpdate();
                final UUID generatedKey;
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if ((resultSet.next())) {
                        generatedKey = resultSet.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("There us no Id in ResultSet");
                    }
                    userEntity.setId(generatedKey);
                    return userEntity;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        try (Connection connection = Databases.connection(CFG.userDataJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?")) {
                preparedStatement.setObject(1, userId);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        UserEntity userEntity = new UserEntity();

                        userEntity.setId(resultSet.getObject("id", UUID.class));
                        userEntity.setUsername(resultSet.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                        userEntity.setFirstname(resultSet.getString("firstname"));
                        userEntity.setSurname(resultSet.getString("surname"));
                        userEntity.setPhoto(resultSet.getBytes("photo"));
                        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                        userEntity.setFullname(resultSet.getString("full_name"));

                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String userName) {
        try (Connection connection = Databases.connection(CFG.userDataJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ?")) {
                preparedStatement.setObject(1, userName);
                preparedStatement.execute();
                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    if (resultSet.next()) {
                        UserEntity userEntity = new UserEntity();

                        userEntity.setId(resultSet.getObject("id", UUID.class));
                        userEntity.setUsername(resultSet.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                        userEntity.setFirstname(resultSet.getString("firstname"));
                        userEntity.setSurname(resultSet.getString("surname"));
                        userEntity.setPhoto(resultSet.getBytes("photo"));
                        userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                        userEntity.setFullname(resultSet.getString("full_name"));

                        return Optional.of(userEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        try (Connection connection = Databases.connection(CFG.userDataJdbcUrl())) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM user WHERE id = ?")) {
                preparedStatement.setObject(1, userEntity.getId());
                preparedStatement.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
