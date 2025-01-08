package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.spend.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    CategoryEntity create(CategoryEntity categoryEntity);

    Optional<CategoryEntity> findCategoryById(UUID id);
}
