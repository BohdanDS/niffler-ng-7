package qa.guru.niffler.service;

import qa.guru.niffler.model.UserJson;

import javax.annotation.Nullable;
import java.util.List;

public interface UserClient {
    UserJson createUser(String username, String password);

    List<UserJson> createIncomeInvitations(UserJson targetUser, int count);

    List<UserJson> createOutcomeInvitations(UserJson targetUser, int count);

    List<UserJson> createFriends(UserJson targetUser, int count);

    List<UserJson> getUsers(@Nullable String username, @Nullable String searchQuery);


}
