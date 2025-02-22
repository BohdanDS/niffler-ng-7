package qa.guru.niffler.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;
import qa.guru.niffler.data.repository.SpendRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static qa.guru.niffler.data.jpa.EntityManagers.em;
@ParametersAreNonnullByDefault
public class SpendRepositoryHibernate implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.spendJdbcUrl());

    @Override
    public @Nonnull SpendEntity createSpend(SpendEntity spendEntity) {
        entityManager.joinTransaction();
        entityManager.persist(spendEntity);
        return spendEntity;
    }

    @Override
    public @Nonnull SpendEntity updateSpend(SpendEntity spendEntity) {
        entityManager.joinTransaction();
        entityManager.merge(spendEntity);
        return spendEntity;
    }

    @Override
    public @Nonnull CategoryEntity createCategory(CategoryEntity categoryEntity) {
        entityManager.joinTransaction();
        entityManager.persist(categoryEntity);
        return categoryEntity;
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(entityManager.find(CategoryEntity.class, id));
    }

    @Override
    public @Nonnull Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
        try {
            TypedQuery<CategoryEntity> query = entityManager.createQuery(
                    "SELECT c FROM CategoryEntity c " +
                            "WHERE c.user.username = :username AND c.category = :name",
                    CategoryEntity.class
            );
            query.setParameter("username", username);
            query.setParameter("name", name);
            return query.getResultList().stream().findFirst();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.ofNullable(entityManager.find(SpendEntity.class, id));
    }

    @Override
    public @Nonnull Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try {
            TypedQuery<SpendEntity> query = entityManager.createQuery(
                    "SELECT s FROM SpendEntity s " +
                            "WHERE s.user.username = :username AND s.description = :description",
                    SpendEntity.class
            );
            query.setParameter("username", username);
            query.setParameter("description", description);
            return query.getResultList().stream().findFirst();
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<SpendEntity> findSpendByUsername(String username) {
        TypedQuery<SpendEntity> query = entityManager.createQuery(
                "SELECT s FROM SpendEntity s WHERE s.username = :username",
                SpendEntity.class
        );
        query.setParameter("username", username);
        return query.getResultList();

    }

    @Override
    public void removeSpend(SpendEntity spendEntity) {
        entityManager.joinTransaction();
        entityManager.remove(spendEntity);
    }

    @Override
    public void removeCategory(CategoryEntity categoryEntity) {
        entityManager.joinTransaction();
        entityManager.remove(categoryEntity);
    }
}
