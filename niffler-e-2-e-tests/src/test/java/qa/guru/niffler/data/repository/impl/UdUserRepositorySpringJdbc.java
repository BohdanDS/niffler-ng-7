package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.data.dao.UdUserDao;
import qa.guru.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import qa.guru.niffler.data.entity.userdata.FriendshipStatus;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.repository.UdUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UdUserRepositorySpringJdbc implements UdUserRepository {

    private final UdUserDao userDaoSpringJdbc = new UdUserDaoSpringJdbc();

    @Override
    public UserEntity createUser(UserEntity user) {
        return userDaoSpringJdbc.createUser(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDaoSpringJdbc.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDaoSpringJdbc.findByUsername(username);
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {

    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        return userDaoSpringJdbc.updateUser(user);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        userDaoSpringJdbc.updateUser(requester);
    }

    @Override
    public void addFriendship(UserEntity requester, UserEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        userDaoSpringJdbc.updateUser(requester);
        userDaoSpringJdbc.updateUser(addressee);
    }

    @Override
    public void removeUser(UserEntity user) {
        userDaoSpringJdbc.updateUser(user);
    }

}
