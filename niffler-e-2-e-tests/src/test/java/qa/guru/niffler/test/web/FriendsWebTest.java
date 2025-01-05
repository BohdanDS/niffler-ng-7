package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.StaticUser;
import qa.guru.niffler.jupiter.extention.meta.WebTest;
import qa.guru.niffler.page.FriendsPage;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.EMPTY;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

@WebTest
public class FriendsWebTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();
    FriendsPage friendsPage = new FriendsPage();

    @Test
    void verifyEmptyFriendTableForNewUser(@UserType(EMPTY) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyEmptyFriendsTable();
    }

    @Test
    void verifyUserWithIncomeRequest(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyIncomeInvitation(user.income_requests());
    }

    @Test
    void verifyUserWithOutcomeRequest(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.switchTab().setValueIntoSearch(user.outcome_requests());
        friendsPage.verifyOutcomeRequest(user.outcome_requests());
    }

    @Test
    void verifyUserWithFriend(@UserType(WITH_FRIEND) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyFriendsList(user.friend());
    }
}
