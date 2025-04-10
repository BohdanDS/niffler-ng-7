package qa.guru.niffler.test.api;

import org.junit.jupiter.api.Test;
import qa.guru.niffler.api.AuthApiClient;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.model.UserJson;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static qa.guru.niffler.utils.OauthUtils.generateCodeChallenge;
import static qa.guru.niffler.utils.OauthUtils.generateCodeVerifier;

public class OAuthTest {
    private final AuthApiClient authApiClient = new AuthApiClient();

    @User
    @Test
    void oAuthTest(UserJson user) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);
        authApiClient.preRequest(codeChallenge);
        String code = authApiClient.login(user.username(), user.testData().password());
        String token = authApiClient.token(code, codeVerifier);

        assertFalse(token.isEmpty());
    }
}
