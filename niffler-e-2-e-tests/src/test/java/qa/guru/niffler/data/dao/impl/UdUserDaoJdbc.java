package qa.guru.niffler.data.dao.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.dao.UdUserDao;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String userName) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {

        List<UserEntity> userEntities = new ArrayList<>();

        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM user")) {
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    UserEntity userEntity = new UserEntity();

                    userEntity.setId(resultSet.getObject("id", UUID.class));
                    userEntity.setUsername(resultSet.getString("username"));
                    userEntity.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    userEntity.setFirstname(resultSet.getString("firstname"));
                    userEntity.setSurname(resultSet.getString("surname"));
                    userEntity.setPhoto(resultSet.getBytes("photo"));
                    userEntity.setPhotoSmall(resultSet.getBytes("photo_small"));
                    userEntity.setFullname(resultSet.getString("full_name"));

                    userEntities.add(userEntity);
                }
                return userEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UserEntity userEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM user WHERE id = ?")) {
            preparedStatement.setObject(1, userEntity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
