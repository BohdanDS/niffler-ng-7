package qa.guru.niffler.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.userdata.FriendshipStatus;
import qa.guru.niffler.data.entity.userdata.UserEntity;
import qa.guru.niffler.data.repository.UdUserRepository;

import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.jpa.EntityManagers.em;

public class UdUserRepositoryHibernate implements UdUserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.userDataJdbcUrl());

    @Override
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.joinTransaction();
        entityManager.persist(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        entityManager.joinTransaction();
        entityManager.merge(userEntity);
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username =: username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
        addressee.addFriends(FriendshipStatus.PENDING, requester);
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.PENDING, addressee);
    }

    @Override
    public void addFriendship(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }

    @Override
    public void removeUser(UserEntity userEntity) {
        entityManager.joinTransaction();
        entityManager.remove(userEntity);
    }
}
