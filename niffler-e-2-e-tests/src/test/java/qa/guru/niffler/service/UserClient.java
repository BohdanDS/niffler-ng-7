package qa.guru.niffler.service;

import qa.guru.niffler.model.UserJson;

public interface UserClient {
    UserJson createUser(String username, String password);

    void createIncomeInvitations(UserJson targetUser, int count);

    void createOutcomeInvitations(UserJson targetUser, int count);

    void createFriends(UserJson targetUser, int count);
}
