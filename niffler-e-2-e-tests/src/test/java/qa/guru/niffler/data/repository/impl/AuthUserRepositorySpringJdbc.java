package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import qa.guru.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.repository.AuthUserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    @Override
    public AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        authUserDao.createUser(authUserEntity);
        authAuthorityDao.create(authUserEntity.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserEntity;
    }

    @Override
    public AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        authUserDao.updateUser(authUserEntity);
        authAuthorityDao.update(authUserEntity.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserEntity;
    }

    @Override
    public Optional<AuthUserEntity> findUserById(UUID id) {
        Optional<AuthUserEntity> userEntity = authUserDao.findUserById(id);
        userEntity.ifPresent(authUserEntity -> {
            authUserEntity.addAuthorities(
                    authAuthorityDao.getAuthorityByUserId(authUserEntity.getId()).toArray(AuthorityEntity[]::new)
            );
        });
        return userEntity;
    }

    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        Optional<AuthUserEntity> userEntity = authUserDao.findUserByUsername(username);
        userEntity.ifPresent(authUserEntity -> {
            authUserEntity.addAuthorities(
                    authAuthorityDao.getAuthorityByUserId(authUserEntity.getId()).toArray(AuthorityEntity[]::new)
            );
        });
        return userEntity;
    }

    @Override
    public void removeUser(AuthUserEntity authUserEntity) {
        authAuthorityDao.deleteUserAuthority(authUserEntity.getId());
        authUserDao.removeUser(authUserEntity);
    }
}
