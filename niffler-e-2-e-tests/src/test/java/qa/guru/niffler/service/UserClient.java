package qa.guru.niffler.service;

import qa.guru.niffler.model.UserJson;

import java.util.List;

public interface UserClient {
    UserJson createUser(String username, String password);

    List<UserJson> createIncomeInvitations(UserJson targetUser, int count);

    void createOutcomeInvitations(UserJson targetUser, int count);

    void createFriends(UserJson targetUser, int count);
}
