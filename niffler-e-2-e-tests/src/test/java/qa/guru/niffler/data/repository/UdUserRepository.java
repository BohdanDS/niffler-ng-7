package qa.guru.niffler.data.repository;

import qa.guru.niffler.data.entity.userdata.FriendshipStatus;
import qa.guru.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UdUserRepository {

    UserEntity createUser(UserEntity userEntity);

    Optional<UserEntity> findById (UUID userId);

    void addFriendshipRecord(UserEntity requester, UserEntity addressee, FriendshipStatus friendshipStatus);

}
