package qa.guru.niffler.service.impl.api;

import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.service.SpendClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;

public class SpendAPIClient implements SpendClient {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public SpendJson createSpend(SpendJson spend) {
        return spendApiClient.addSpend(spend);
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }

    @Override
    public List<SpendJson> findAllByUsername(String username) {
        return spendApiClient.getAllSpendByUsername(username);
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        spendApiClient.removeSpend(spend.username(), singletonList(spend.id().toString()));
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return spendApiClient.createCategory(category);
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }
}
