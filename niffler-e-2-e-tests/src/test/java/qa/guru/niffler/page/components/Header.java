package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import qa.guru.niffler.page.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Header {
    private final SelenideElement
            self = $("#root header"),
            profileIcon = self.$("button"),
            spendingBtn = self.$("[href='/spending']"),
            profileLink = $("a[href='/profile']"),
            friendsLink = $("a[href='/friends']"),
            allPeopleLink = $("a[href='/all']"),
            signOutButton = $$("li").find(text("Sign out"));
    ;


    public void clickOnProfileIcon() {
        profileIcon.click();
    }

    public FriendsPage toFriendsPage() {
        friendsLink.click();
        return new FriendsPage();
    }

    public ProfilePage toProfilePage() {
        profileLink.click();
        return new ProfilePage();
    }

    public AllPeoplePage toAllPeoplePage() {
        allPeopleLink.click();
        return new AllPeoplePage();
    }

    public LoginPage toLoginPage() {
        signOutButton.click();
        return new LoginPage();
    }

    public SpendingPage toSpendingPage() {
        signOutButton.click();
        return new SpendingPage();
    }

}
