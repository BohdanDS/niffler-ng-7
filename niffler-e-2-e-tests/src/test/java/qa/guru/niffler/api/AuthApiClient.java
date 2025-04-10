package qa.guru.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import qa.guru.niffler.api.core.RestClient;
import qa.guru.niffler.api.core.ThreadSafeCookieStore;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static qa.guru.niffler.utils.OauthUtils.getCodeFromRedirectUrl;


public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true);
        this.authApi = create(AuthApi.class);
    }

    private final static String RESPONSE_TYPE = "code";
    private final static String CLIENT_ID = "client";
    private final static String SCOPE = "openid";
    private final static String REDIRECT_URI = CFG.frontUrl() + "/authorized";
    private static final String GRANT_TYPE = "authorization_code";
    private final static String CODE_CHALLENGE_METHOD = "S256";

    public void preRequest(String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                            RESPONSE_TYPE,
                            CLIENT_ID,
                            SCOPE,
                            REDIRECT_URI,
                            codeChallenge,
                            CODE_CHALLENGE_METHOD)
                    .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public String login(String username, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                    username,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());

        return getCodeFromRedirectUrl(response.raw().request().url().toString());
    }

    public String token(String code, String codeVerifier) {
        final Response<JsonNode> response;
        try {
            response = authApi.token(code, REDIRECT_URI, codeVerifier, GRANT_TYPE, CLIENT_ID).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());

        assertFalse(Objects.requireNonNull(response.body()).isEmpty());

        return response.body().path("id_token").asText();
    }

}
