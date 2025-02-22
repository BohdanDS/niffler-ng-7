package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import qa.guru.niffler.page.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    private final SelenideElement
            self = $("#root header"),
            profileIcon = self.$("button"),
            spendingBtn = self.$("[href='/spending']"),
            headerMenu = $("ul[role='menu']");
    private final SelenideElement profileLink = headerMenu.findAll("li").find(text("Profile"));
    private final SelenideElement friendsLink = headerMenu.findAll("li").find(text("Friends"));
    private final SelenideElement allPeopleLink = headerMenu.findAll("li").find(text("All People"));
    private final SelenideElement signOutBtn = headerMenu.findAll("li").find(text("Sign out"));

    public Header() {
        super($("#root header"));
    }




    public Header clickOnProfileIcon() {
        profileIcon.click();
        return this;
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
        signOutBtn.click();
        return new LoginPage();
    }

    public SpendingPage toSpendingPage() {
        spendingBtn.click();
        return new SpendingPage();
    }

}
