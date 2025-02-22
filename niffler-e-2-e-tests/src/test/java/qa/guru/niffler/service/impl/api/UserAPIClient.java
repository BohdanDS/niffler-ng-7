package qa.guru.niffler.service.impl.api;

import io.qameta.allure.Step;
import qa.guru.niffler.api.UserApiClient;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.TestData;
import qa.guru.niffler.model.UserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;

@ParametersAreNonnullByDefault
public class UserAPIClient implements qa.guru.niffler.service.UserClient {

    private final UserApiClient userApiClient = new UserApiClient();

    @Override
    @Step("Создать пользователя")
    public @Nullable UserJson createUser(String username, String password) {
        userApiClient.createUser(username, password);
        return new UserJson(null, username, null, null, null, CurrencyValues.RUB, null, null, null, new TestData(password, null, null));
    }

    @Override
    @Step("Создать входящую заявку в друзья")
    public @Nullable List<UserJson> createIncomeInvitations(UserJson targetUser, int count) {

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
    @Step("Создать исходящую заявку в друзья")
    public @Nullable List<UserJson> createOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> outcomeInvitaitonList = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = randomUserName();
                String password = "123";

                UserJson user = createUser(username, password);

                userApiClient.sendInvitation(username, targetUser.username());
                outcomeInvitaitonList.add(user);
            }
        }
        return outcomeInvitaitonList;
    }

    @Override
    @Step("Создать дружбу")
    public @Nullable List<UserJson> createFriends(UserJson targetUser, int count) {
        List<UserJson> friendsList = new ArrayList<>();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String username = randomUserName();
                String password = "123";

                UserJson user = createUser(username, password);

                userApiClient.sendInvitation(targetUser.username(), username);
                userApiClient.acceptInvitation(username, targetUser.username());
                friendsList.add(user);
            }
        }
        return friendsList;
    }
}
