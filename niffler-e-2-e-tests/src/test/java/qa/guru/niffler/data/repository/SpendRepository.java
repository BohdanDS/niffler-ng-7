package qa.guru.niffler.data.repository;

import qa.guru.niffler.data.entity.spend.CategoryEntity;
import qa.guru.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {
    SpendEntity createSpend(SpendEntity spend);

    SpendEntity updateSpend(SpendEntity spend);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

    Optional<SpendEntity> findSpendById(UUID id);

    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    List<SpendEntity> findSpendByUsername(String username);

    void removeSpend(SpendEntity spend);

    void removeCategory(CategoryEntity category);
}
