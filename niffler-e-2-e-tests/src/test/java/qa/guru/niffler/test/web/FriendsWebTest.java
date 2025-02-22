package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.extension.meta.*;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.FriendsPage;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

import java.util.List;

@WebTest
public class FriendsWebTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();
    FriendsPage friendsPage = new FriendsPage();

//    @Test
//    void verifyEmptyFriendTableForNewUser(@UserType(EMPTY) StaticUser user) {
//        loginPage.login(user.username(), user.password());
//        mainPage.getHeader().clickOnProfileIcon();
//        mainPage.getUserProfileMenuComponent().clickFriendsLink();
//        friendsPage.verifyEmptyFriendsTable();
//    }
//
//    @Test
//    void verifyUserWithIncomeRequest(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
//        loginPage.login(user.username(), user.password());
//        mainPage.getHeader().clickOnProfileIcon();
//        mainPage.getUserProfileMenuComponent().clickFriendsLink();
//        friendsPage.verifyIncomeInvitation(user.income_requests());
//    }
//
//    @Test
//    void verifyUserWithOutcomeRequest(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
//        loginPage.login(user.username(), user.password());
//        mainPage.getHeader().clickOnProfileIcon();
//        mainPage.getUserProfileMenuComponent().clickFriendsLink();
//        friendsPage.clickFriendsTab().setValueIntoSearch(user.outcome_requests());
//        friendsPage.verifyOutcomeRequest(user.outcome_requests());
//    }
//
//    @Test
//    void verifyUserWithFriend(@UserType(WITH_FRIEND) StaticUser user) {
//        loginPage.login(user.username(), user.password());
//        mainPage.getHeader().clickOnProfileIcon();
//        mainPage.getUserProfileMenuComponent().clickFriendsLink();
//        friendsPage.verifyFriendsList(user.friend());
//    }


    @User
    @IncomeRequest(count = 2)
    @Test
    void newUserWithIncomeInvitationTest(UserJson user, List<String> requests) {
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toFriendsPage().verifyIncomeInvitation(requests);
    }

    @User
    @OutcomeRequest(count = 3)
    @Test
    void newUserWithOutcomeRequests(UserJson user, List<String> requests) {
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toAllPeoplePage().verifyOutcomeInvitations(requests);
    }

    @User
    @FriendRequest(count = 2)
    @Test
    void newUserWithFriendsTest(UserJson user, List<String> requests) {
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toFriendsPage().verifyFriendsList(requests);
    }

    @User
    @IncomeRequest(count = 1)
    @Test
    void declineInvitation(UserJson user, List<String> requests) {
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toFriendsPage().declineInvitation(requests.getFirst());
    }

    @User
    @IncomeRequest(count = 1)
    @Test
    void acceptInvitation(UserJson user, List<String> requests) {
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toFriendsPage().acceptInvitation(requests.getFirst()).verifyFriendsList(requests);
    }

}
