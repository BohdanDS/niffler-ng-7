package qa.guru.niffler.api;

import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public @Nullable SpendJson addSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    public @Nullable SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.updateSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public @Nullable SpendJson getSpendByIdAndUsername(String spendId, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(spendId, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public @Nonnull List<SpendJson> getAllSpends(String username,
                                        @Nullable CurrencyValues currencyValues,
                                        @Nullable String from,
                                        @Nullable String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, currencyValues, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public @Nonnull List<SpendJson> getAllSpendByUsername(String username) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, null, null, null).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    public void removeSpend(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpend(username, ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    public @Nullable CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public @Nonnull List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategories(username, excludeArchived).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }


}
