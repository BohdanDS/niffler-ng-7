package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.IncomeExtension;
import qa.guru.niffler.jupiter.extention.UserExtension;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.StaticUser;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType;
import qa.guru.niffler.jupiter.extention.meta.IncomeRequest;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.jupiter.extention.meta.WebTest;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.FriendsPage;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

import java.util.List;

import static qa.guru.niffler.jupiter.extention.UsersQueueExtension.UserType.Type.*;

@WebTest
public class    FriendsWebTest {

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
    void newUserWithIncomeInvitationTest(UserJson user, List<String> requests){
        System.out.println(requests);
        loginPage.login(user.username(), user.testData().password());
        mainPage.getHeader().clickOnProfileIcon().toFriendsPage().verifyIncomeInvitation(requests);

    }
}
