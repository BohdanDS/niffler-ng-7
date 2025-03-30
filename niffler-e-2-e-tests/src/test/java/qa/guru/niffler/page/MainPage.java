package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.Getter;
import qa.guru.niffler.page.components.Header;
import qa.guru.niffler.page.components.SpendingTable;
import qa.guru.niffler.page.components.StatComponent;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class MainPage extends BasePage<MainPage>{

    @Getter
    private final Header header = new Header();

    @Getter
    private final SpendingTable spendingTable = new SpendingTable();

    @Getter
    private final StatComponent statComponent = new StatComponent();

    @Getter
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    private final SelenideElement statistic = $("#stat"),

    deleteBtn = $("#delete"),

            spendings = $("#spendings");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 5).click();

        return new EditSpendingPage();
    }

    public EditSpendingPage selectSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td", 1).click();

        return new EditSpendingPage();
    }

    public void checkUpdatedDescription(String updatedDescription) {
        tableRows.find(text(updatedDescription)).shouldBe(visible);
    }

    public void checkSuccessLogin() {
        statistic.shouldBe(visible);
        spendings.shouldBe(visible);
    }

    @Step("Проверить что spending с категорией {spendingCategory} отображается в таблице")
    public void checkSpendingInTable(String spendingCategory){
        tableRows.findBy(text(spendingCategory)).shouldBe(visible);
    }

    @Override
    public MainPage checkThatPageLoaded() {
        return null;
    }
}

