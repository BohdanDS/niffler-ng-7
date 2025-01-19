package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.userdata.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserDao {

    UserEntity createUser(UserEntity userEntity);

    Optional<UserEntity> findById(UUID userId);

    Optional<UserEntity> findByUsername(String userName);

    List<UserEntity> findAll();

    void deleteUser(UserEntity userEntity);

}
