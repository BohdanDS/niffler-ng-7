package qa.guru.niffler.data.repository;

import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    AuthUserEntity createUser(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findUserById(UUID id);

//    List<AuthUserEntity> findAll();
//
//    void deleteUser(UUID id);

}
