package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.userdata.FriendshipEntity;
import qa.guru.niffler.data.entity.userdata.FriendshipStatus;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.mapper.UserDataUserEntityRowMapper;
import qa.guru.niffler.data.repository.UdUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.tpl.Connections.holder;

public class UdUserRepositoryJdbc implements UdUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setString(1, userEntity.getUsername());
            preparedStatement.setString(2, userEntity.getCurrency().name());
            preparedStatement.setString(3, userEntity.getFirstname());
            preparedStatement.setString(4, userEntity.getSurname());
            preparedStatement.setBytes(5, userEntity.getPhoto());
            preparedStatement.setBytes(6, userEntity.getPhotoSmall());
            preparedStatement.setString(7, userEntity.getFullname());

            preparedStatement.executeUpdate();

            final UUID generatedKey;

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find user id in ResultSet");
                }
            }

            userEntity.setId(generatedKey);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        try (PreparedStatement preparedStatement = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" u LEFT JOIN friendship f ON (u.id = f.addressee_id OR u.id = f.requester_id)\n" +
                        "WHERE u.id = ?"
        )) {
            preparedStatement.setObject(1, userId);
            preparedStatement.execute();

            UserEntity userEntity = null;

            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    if (userEntity == null) {
                        System.out.println("if");
                        userEntity = UserDataUserEntityRowMapper.instance.mapRow(resultSet, 1);
                    }
                    if (resultSet.getString("status") != null) {

                        if (resultSet.getObject("requester_id", UUID.class).equals(userEntity.getId())) {
                            System.out.println("if");
                            FriendshipEntity friendship = new FriendshipEntity();
                            friendship.setRequester(userEntity);

                            UserEntity addressee = new UserEntity();

                            addressee.setId(resultSet.getObject("addressee_id", UUID.class));

                            friendship.setAddressee(addressee);
                            friendship.setCreatedDate(resultSet.getDate("created_date"));
                            friendship.setStatus(FriendshipStatus.valueOf(resultSet.getString("status")));

                            userEntity.getFriendshipRequests().add(friendship);
                        } else {
                            System.out.println("else");
                            FriendshipEntity friendship = new FriendshipEntity();
                            friendship.setAddressee(userEntity);

                            UserEntity requester = new UserEntity();

                            requester.setId(resultSet.getObject("requester_id", UUID.class));
                            friendship.setRequester(requester);
                            friendship.setCreatedDate(resultSet.getDate("created_date"));
                            friendship.setStatus(FriendshipStatus.valueOf(resultSet.getString("status")));

                            userEntity.getFriendshipAddressees().add(friendship);

                        }
                    }
                }
            }
            return Optional.ofNullable(userEntity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {

    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {

    }

    @Override
    public void addFriendship(UserEntity requester, UserEntity addressee) {

    }

//    @Override
//    public void addFriendshipRecord(UserEntity requester, UserEntity addressee, FriendshipStatus friendshipStatus) {
//
//    }
}
