package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

    AuthUserEntity createUser(AuthUserEntity authUserEntity);

    Optional<AuthUserEntity> findUserById(UUID id);

    List<AuthUserEntity> findAll();

    void deleteUser(UUID id);


}
