package qa.guru.niffler.service.impl.api;

import io.qameta.allure.Step;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.service.SpendClient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;

@ParametersAreNonnullByDefault
public class SpendAPIClient implements SpendClient {

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    @Step("Создать spend")
    public @Nullable SpendJson createSpend(SpendJson spend) {
        return spendApiClient.addSpend(spend);
    }

    @Override
    @Step("Найти spend по ID")
    public @Nullable Optional<SpendJson> findSpendById(UUID id) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }

    @Override
    @Step("Найти все траты по имени пользователя")
    public @Nullable List<SpendJson> findAllByUsername(String username) {
        return spendApiClient.getAllSpendByUsername(username);
    }

    @Override
    @Step("Удалить трату")
    public void deleteSpend(SpendJson spend) {
        spendApiClient.removeSpend(spend.username(), singletonList(spend.id().toString()));
    }

    @Override
    @Step("Создать категорию")
    public @Nullable CategoryJson createCategory(CategoryJson category) {
        return spendApiClient.createCategory(category);
    }

    @Override
    @Step("Найти категорию по имени пользователя и имени")
    public @Nullable Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }

    @Override
    @Step("Удалить категорию")
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Invalid operation for spend");
    }
}
