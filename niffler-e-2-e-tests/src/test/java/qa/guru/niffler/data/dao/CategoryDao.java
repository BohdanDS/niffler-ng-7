package qa.guru.niffler.data.dao;

import qa.guru.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    CategoryEntity createCategory(CategoryEntity categoryEntity);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    List<CategoryEntity> findAllByUsername(String username);

    void deleteCategory(CategoryEntity categoryEntity);
}
