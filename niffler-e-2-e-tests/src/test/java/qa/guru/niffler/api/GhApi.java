package qa.guru.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import retrofit2.Call;
import retrofit2.http.*;

public interface GhApi {

    @GET("/repos/bohdands/niffler-ng-7/issues/{issue_number}")
    @Headers({
            "Accept: application/vnd.github+json",
            "X-GitHub-Api-Version: 2022-11-28"
    })
    Call<JsonNode> issue(@Header("Authorization") String bearerToken,
                         @Path("issue_number") String issueNumber);
}
