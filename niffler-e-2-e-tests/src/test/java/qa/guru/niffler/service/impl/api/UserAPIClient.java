package qa.guru.niffler.service.impl.api;

import qa.guru.niffler.api.UserApiClient;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.TestData;
import qa.guru.niffler.model.UserJson;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;

public class UserAPIClient implements qa.guru.niffler.service.UserClient {

    private final UserApiClient userApiClient = new UserApiClient();

    @Override
    public UserJson createUser(String username, String password) {
        userApiClient.createUser(username, password);
        return new UserJson(null, username, null, null, null, CurrencyValues.RUB, null, null, null, new TestData(password, null, null));
    }

    @Override
    public List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {

        List<UserJson> incomeInvitaitonList = new ArrayList<>();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = randomUserName();
                String password = "123";

                UserJson user = createUser(username, password);

                userApiClient.sendInvitation(targetUser.username(), username);
                incomeInvitaitonList.add(user);
            }
        }
        return incomeInvitaitonList;
    }

    @Override
    public void createOutcomeInvitations(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = randomUserName();
                String password = "123";

                createUser(username, password);

                userApiClient.sendInvitation(username, targetUser.username());
            }
        }
    }

    @Override
    public void createFriends(UserJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = randomUserName();
                String password = "12345";

                createUser(username, password);
                userApiClient.sendInvitation(username, targetUser.username());
                userApiClient.sendInvitation(targetUser.username(), username);
                userApiClient.acceptInvitation(username, targetUser.username());
                userApiClient.acceptInvitation(targetUser.username(), username);
            }
        }
    }

    public void createFriend(UserJson requester, UserJson addressee) {
        userApiClient.sendInvitation(requester.username(), addressee.username());
        userApiClient.sendInvitation(addressee.username(), requester.username());
        userApiClient.acceptInvitation(requester.username(), addressee.username());
        userApiClient.acceptInvitation(addressee.username(), requester.username());
    }

    public void createInvitation(UserJson requester, UserJson addressee) {
        userApiClient.sendInvitation(requester.username(), addressee.username());
    }
}
