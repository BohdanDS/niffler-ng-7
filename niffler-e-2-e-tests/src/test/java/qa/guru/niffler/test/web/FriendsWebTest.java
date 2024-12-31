package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.BrowserExtension;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.StaticUser;
import qa.guru.niffler.page.FriendsPage;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.EMPTY;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();
    FriendsPage friendsPage = new FriendsPage();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void verifyEmptyFriendTableForNewUser(@UserType(EMPTY) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyEmptyFriendsTable();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void verifyUserWithIncomeRequest(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyIncomeInvitation(user.income_requests());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void verifyUserWithOutcomeRequest(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.switchTab().setValueIntoSearch(user.outcome_requests());
        friendsPage.verifyOutcomeRequest(user.outcome_requests());
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void verifyUserWithFriend(@UserType(WITH_FRIEND) StaticUser user) {
        loginPage.login(user.username(), user.password());
        mainPage.getHeaderComponent().clickOnProfileIcon();
        mainPage.getUserProfileMenuComponent().clickFriendsLink();
        friendsPage.verifyFriendsList(user.friend());
    }
}
