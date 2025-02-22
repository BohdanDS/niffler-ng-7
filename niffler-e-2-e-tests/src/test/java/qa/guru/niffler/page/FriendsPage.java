package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage extends BasePage<FriendsPage> {

    private final SelenideElement
            emptyFriendsTable = $("#simple-tabpanel-friends"),
            friendsTab = $("a[aria-selected='false']"),
            allPeopleTab = $("a[aria-selected='true']"),
            popUp = $("div[role='dialog']");
    private final ElementsCollection allPeopleRows = $$("#all tr"),
            allFriendsRows = $$("#friends tr"),
            incomeRequestsRows = $$("#requests tr");

    @Step("Подтвердить что таблица друзей пустая")
    public void verifyEmptyFriendsTable() {
        emptyFriendsTable.shouldBe(visible).shouldHave(text("There are no users yet"));
    }

    @Step("Проверить список входящих приглашений")
    public void verifyIncomeInvitation(List<String> names) {
        for (String name : names) {
            incomeRequestsRows.findBy(text(name)).$("p").shouldHave(text(name)).shouldBe(visible);
        }

    }

    @Step("Проверить список друзей")
    public void verifyFriendsList(List<String> names) {
        for (String name : names) {
            allFriendsRows.findBy(text(name)).$("p").shouldHave(text(name)).shouldBe(visible);
        }
    }

    @Step("Принимаем заявку в друзья от {username}")
    public FriendsPage acceptInvitation(String username) {
        incomeRequestsRows.findBy(text(username)).$(byText("Accept")).click();
        return this;
    }

    @Step("Отклоняем заявку в друзья от {username}")
    public FriendsPage declineInvitation(String username) {
        incomeRequestsRows.findBy(text(username)).$(byText("Decline")).click();
        confirmDecline();
        verifyEmptyFriendsTable();
        return this;
    }

    public FriendsPage clickFriendsTab() {
        friendsTab.click();
        return this;
    }

    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }


    @Step("Подтвердить отклонение заявки в друзья")
    private void confirmDecline() {
        popUp.$(byText("Decline")).click();
    }

    @Override
    public FriendsPage checkThatPageLoaded() {
        return null;
    }
}
