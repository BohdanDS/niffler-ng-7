package qa.guru.niffler.data.repository;

import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    AuthUserEntity createUser(AuthUserEntity authUserEntity);
    AuthUserEntity updateUser(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findUserById(UUID id);

    Optional<AuthUserEntity> findUserByUsername(String username);

    void removeUser(AuthUserEntity authUserEntity);

}
