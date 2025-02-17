package qa.guru.niffler.data.repository.impl;

import qa.guru.niffler.data.dao.AuthAuthorityDao;
import qa.guru.niffler.data.dao.AuthUserDao;
import qa.guru.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import qa.guru.niffler.data.dao.impl.AuthUserDaoJdbc;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.entity.auth.AuthorityEntity;
import qa.guru.niffler.data.repository.AuthUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;
@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

    @Override
    public @Nonnull AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        authUserDao.createUser(authUserEntity);
        authAuthorityDao.create(authUserEntity.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserEntity;
    }

    @Override
    public @Nonnull AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        authUserDao.updateUser(authUserEntity);
        authAuthorityDao.update(authUserEntity.getAuthorities().toArray(AuthorityEntity[]::new));
        return authUserEntity;
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserById(UUID id) {
        Optional<AuthUserEntity> userEntity = authUserDao.findUserById(id);
        userEntity.ifPresent(authUserEntity -> {
            authUserEntity.addAuthorities(
                    authAuthorityDao.getAuthorityByUserId(authUserEntity.getId()).toArray(AuthorityEntity[]::new)
            );
        });
        return userEntity;
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserByUsername(String username) {
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
