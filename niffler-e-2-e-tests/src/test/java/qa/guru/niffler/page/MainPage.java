package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import qa.guru.niffler.page.components.HeaderComponent;
import qa.guru.niffler.page.components.UserProfileMenuComponent;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class MainPage {

    @Getter
    private final HeaderComponent headerComponent = new HeaderComponent();

    @Getter
    private final UserProfileMenuComponent userProfileMenuComponent = new UserProfileMenuComponent();
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    private final SelenideElement statistic = $("#stat"),
            spendings = $("#spendings");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 5).click();

        return new EditSpendingPage();
    }

    public void checkUpdatedDescription(String updatedDescription) {
        tableRows.find(text(updatedDescription)).shouldBe(visible);
    }

    public void checkSuccessLogin() {
        statistic.shouldBe(visible);
        spendings.shouldBe(visible);
    }

}

