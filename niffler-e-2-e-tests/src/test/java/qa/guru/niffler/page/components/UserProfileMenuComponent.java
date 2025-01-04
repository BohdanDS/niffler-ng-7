package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class UserProfileMenuComponent {
    private final SelenideElement
            profileLink = $("a[href='/profile']"),
            friendsLink = $("a[href='/people/friends']"),
            allPeopleLink = $("a[href='/people/all']");

    public void clickProfileLink() {
        profileLink.click();
    }

    public void clickFriendsLink() {
        friendsLink.click();
    }

    public void clickAllPeopleLink() {
        allPeopleLink.click();
    }
}
