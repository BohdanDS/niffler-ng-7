package qa.guru.niffler.api;

import okhttp3.OkHttpClient;
import org.apache.hc.core5.http.HttpStatus;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.TestData;
import qa.guru.niffler.model.UserJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiClient {

    private static final Config CFG = Config.getInstance();

    OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new MyCookieJar())
            .build();

    private final Retrofit retrofitAuth = new Retrofit.Builder()
            .client(client)
            .baseUrl(CFG.authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final Retrofit retrofitUD = new Retrofit.Builder()
            .client(client)
            .baseUrl(CFG.userDataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UserApi usersAuthApi = retrofitAuth.create(UserApi.class);
    private final UserApi usersUDApi = retrofitUD.create(UserApi.class);


    public UserJson createUser(String username, String password) {
        String csrfToken;
        Response<Void> response;

        try {
            csrfToken = usersAuthApi.getToken()
                    .execute()
                    .headers()
                    .get("X-XSRF-TOKEN");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            response = usersAuthApi.createUser(username, password, password, csrfToken)
                    .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());

        return new UserJson(
                null,
                username,
                null,
                null,
                null,
                CurrencyValues.RUB,
                null,
                null,
                null,
                new TestData(password, null, null)
        );
    }

    public UserJson updateUser(UserJson user) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.updateUser(user)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public UserJson getUser(String username) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.getUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public List<UserJson> getAllUsers(String username) {
        final Response<List<UserJson>> response;
        try {
            response = usersUDApi.getUsers(username, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public UserJson sendInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.sendInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public UserJson acceptInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.acceptInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public UserJson declineInvitation(String username, String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.declineInvitation(username, targetUsername)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }
}
