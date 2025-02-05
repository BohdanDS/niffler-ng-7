package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

    AuthUserEntity createUser(AuthUserEntity authUserEntity);

    AuthUserEntity updateUser(AuthUserEntity authUserEntity);

    List<AuthUserEntity> findAll();

    Optional<AuthUserEntity> findUserById(UUID id);

    Optional<AuthUserEntity> findUserByUsername(String username);

    void removeUser(AuthUserEntity authUserEntity);

}
