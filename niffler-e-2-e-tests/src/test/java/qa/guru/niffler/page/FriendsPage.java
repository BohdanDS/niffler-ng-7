package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final SelenideElement searchInput = $("input[aria-label='search']"),
            emptyFriendsTable = $("#simple-tabpanel-friends"),
            tabsSwitcher = $("a[aria-selected='false']");
    private final ElementsCollection allPeopleRows = $$("#all tr"),
            allFriendsRows = $$("#friends tr"),
            incomeRequestsRows = $$("#requests tr");

    public void verifyEmptyFriendsTable() {
        emptyFriendsTable.shouldBe(visible).shouldHave(text("There are no users yet"));
    }

    public void verifyIncomeInvitation(String userName) {
        incomeRequestsRows.findBy(text(userName)).$("p").shouldHave(text(userName)).shouldBe(visible);
    }

    public void setValueIntoSearch(String value) {
        searchInput.setValue(value).sendKeys(Keys.ENTER);
    }

    public void verifyOutcomeRequest(String userName) {
        allPeopleRows.findBy(text(userName)).$("span").shouldHave(text("Waiting...")).shouldBe(visible);
    }

    public void verifyFriendsList(String userName) {
        allFriendsRows.findBy(text(userName)).$("p").shouldHave(text(userName)).shouldBe(visible);
    }

    public FriendsPage switchTab() {
        tabsSwitcher.click();
        return this;
    }

}
