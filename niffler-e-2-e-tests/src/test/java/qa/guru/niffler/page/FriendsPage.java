package qa.guru.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final SelenideElement searchInput = $("input[aria-label='search']"),
            emptyFriendsTable = $("#simple-tabpanel-friends"),
            searchInputField = $("input[aria-label=\"search\"]"),
            friendsTab = $("a[aria-selected='false']"),
            allPeopleTab = $("a[aria-selected='true']");
    private final ElementsCollection allPeopleRows = $$("#all tr"),
            allFriendsRows = $$("#friends tr"),
            incomeRequestsRows = $$("#requests tr");

    public void verifyEmptyFriendsTable() {
        emptyFriendsTable.shouldBe(visible).shouldHave(text("There are no users yet"));
    }

    public void verifyIncomeInvitation(List<String> names) {
        for (String name : names) {
            incomeRequestsRows.findBy(text(name)).$("p").shouldHave(text(name)).shouldBe(visible);
        }

    }

    public void setValueIntoSearch(String value) {
        searchInput.setValue(value).pressEnter();
    }

    public void verifyOutcomeRequest(String userName) {
        allPeopleRows.findBy(text(userName)).$("span").shouldHave(text("Waiting...")).shouldBe(visible);
    }

    public void verifyFriendsList(String userName) {
        if (allFriendsRows.findBy(text(userName)).isDisplayed()) {
            System.out.println("123");
            allFriendsRows.findBy(text(userName)).$("p").shouldHave(text(userName));
        } else {
            searchInputField.setValue(userName).pressEnter();
            allPeopleRows.shouldHave(CollectionCondition.sizeGreaterThan(0));

            allPeopleRows.findBy(text(userName)).$("p").shouldHave(text(userName));
        }
    }

    public FriendsPage clickFriendsTab() {
        friendsTab.click();
        return this;
    }
    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

}
