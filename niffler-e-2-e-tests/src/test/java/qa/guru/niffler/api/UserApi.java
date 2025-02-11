package qa.guru.niffler.api;

import okhttp3.ResponseBody;
import qa.guru.niffler.model.RegistrationJson;
import qa.guru.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface UserApi {

    @GET("/register")
    Call<ResponseBody> getToken();

    @POST("/register")
    @FormUrlEncoded
    Call<Void> createUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("passwordSubmit") String passwordSubmit,
            @Field("_csrf") String csrf);

    @GET("/internal/users/current")
    Call<UserJson> getUser(@Query("username") String username);

    @GET("/internal/users/all")
    Call<List<UserJson>> getUsers(@Query("username") String username,
                                  @Query("searchQuery") String searchQuery);

    @POST("internal/users/update")
    Call<UserJson> updateUser(@Body UserJson user);

    @POST("/internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username,
                                  @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username,
                                    @Query("targetUsername") String targetUsername);

    @POST("/internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username,
                                     @Query("targetUsername") String targetUsername);
}
