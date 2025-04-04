package qa.guru.niffler.service;

import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    Optional<SpendJson> findSpendById(UUID id);

    List<SpendJson> findAllByUsername(String username);

    void deleteSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName);

    void deleteCategory(CategoryJson category);
}
