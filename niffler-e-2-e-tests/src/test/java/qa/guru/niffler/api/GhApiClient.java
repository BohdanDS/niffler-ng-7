package qa.guru.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import qa.guru.niffler.config.Config;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GhApiClient {

    private final static String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().ghUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public @Nonnull String issueState(String issueNumber) {
        final Response<JsonNode> response;
        try {
            response = ghApi.issue(
                            "Bearer" + System.getenv(GH_TOKEN_ENV),
                            issueNumber)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError();
        }
        assertEquals(200, response.code());
        return Objects.requireNonNull(response.body()).get("state").asText();
    }


}
