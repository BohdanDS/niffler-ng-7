package qa.guru.niffler.api;

import org.apache.hc.core5.http.HttpStatus;
import qa.guru.niffler.api.core.RestClient.EmtyRestClient;
import qa.guru.niffler.api.core.ThreadSafeCookieStore;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.TestData;
import qa.guru.niffler.model.UserJson;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient {

    private static final Config CFG = Config.getInstance();

    private final UserApi usersAuthApi = new EmtyRestClient(CFG.authUrl()).create(UserApi.class);
    private final UserApi usersUDApi = new EmtyRestClient(CFG.userDataUrl()).create(UserApi.class);


    public @Nonnull UserJson createUser(String username, String password) {
        Response<Void> response;
        try {
            usersAuthApi.getToken().execute();
            response = usersAuthApi.createUser(username, password, password, ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public @Nullable UserJson updateUser(UserJson user) {
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

    public @Nullable UserJson getUser(String username) {
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

    public List<UserJson> getAllUsers(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = usersUDApi.getUsers(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }

    public @Nullable UserJson sendInvitation(String targetUsername, String username) {
        final Response<UserJson> response;
        try {
            response = usersUDApi.sendInvitation(targetUsername, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_SUCCESS, response.code());
        return response.body();
    }

    public @Nullable UserJson acceptInvitation(String username, String targetUsername) {
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

    public @Nullable UserJson declineInvitation(String username, String targetUsername) {
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
