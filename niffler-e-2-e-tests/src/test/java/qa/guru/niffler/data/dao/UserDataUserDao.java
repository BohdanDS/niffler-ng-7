package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDataUserDao {

    UserEntity createUser(UserEntity userEntity);

    Optional<UserEntity> findById(UUID userId);

    Optional<UserEntity> findByUsername(String userName);

    void deleteUser(UserEntity userEntity);

}
