package qa.guru.niffler.api;

import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8093")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson addSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    public SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.updateSpend(spend)
                    .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpendById(String spendId, String username){
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(spendId, username)
                    .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<SpendJson> getAllSpends(String username, CurrencyValues currencyValues, String from, String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, currencyValues, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void removeSpend(String username, List<String> ids){
        final Response<Void> response;
        try {
            response = spendApi.removeSpend(username, ids).execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

    public CategoryJson createCategory (CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.createCategory(category).execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public CategoryJson updateCategory (CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category).execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<CategoryJson> getAllCategories (String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getAllCategories(username, excludeArchived).execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }


}
