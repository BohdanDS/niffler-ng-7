package qa.guru.niffler.data.repository;

import qa.guru.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UdUserRepository {

    UserEntity createUser(UserEntity userEntity);

    UserEntity updateUser(UserEntity userEntity);

    Optional<UserEntity> findById (UUID userId);

    Optional<UserEntity> findByUsername(String username);

    void addIncomeInvitation(UserEntity requester, UserEntity addressee);
    void addOutcomeInvitation(UserEntity requester, UserEntity addressee);
    void addFriendship(UserEntity requester, UserEntity addressee);

    void removeUser(UserEntity userEntity);

}
