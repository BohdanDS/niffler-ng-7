package qa.guru.niffler.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.auth.AuthUserEntity;
import qa.guru.niffler.data.repository.AuthUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.authJdbcUrl());

    @Override
    public @Nonnull AuthUserEntity createUser(AuthUserEntity authUserEntity) {
        entityManager.joinTransaction();
        entityManager.persist(authUserEntity);
        return authUserEntity;
    }

    @Override
    public @Nonnull AuthUserEntity updateUser(AuthUserEntity authUserEntity) {
        entityManager.joinTransaction();
        entityManager.merge(authUserEntity);
        return authUserEntity;
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserById(UUID id) {
        return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findUserByUsername(String username) {
        try {
            return Optional.of(entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username =: username", AuthUserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void removeUser(AuthUserEntity authUserEntity) {
        entityManager.joinTransaction();
        entityManager.remove(authUserEntity);
    }
}
