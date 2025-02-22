package qa.guru.niffler.page.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import qa.guru.niffler.page.EditSpendingPage;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpendingTable {
    private final SelenideElement self = $("#spendings tbody");
    private final ElementsCollection tableRows = self.$$("tr");
    private final SelenideElement periodInput = $("#period");
    private final ElementsCollection dropdownList = $$("ul[role=listbox]");
    private final SelenideElement deleteBtn = $("#delete");
    private final Search searchField = new Search();

    @Step("Фильтр таблицы по периоду: {period}")
    public SpendingTable selectPeriod(PeriodValues period) {
        periodInput.click();
        dropdownList.find(text(period.name())).click();
        return this;
    }

    @Step("Редактируем трату с описанием: {description}")
    public EditSpendingPage editSpending(String description) {
        findSpendingRow(description).$$("td").get(0).click();
        return new EditSpendingPage();
    }

    @Step("Удаляем трату с описанием: {description}")
    public SpendingTable deleteSpending(String description) {
        findSpendingRow(description).$$("td").get(5).click();
        deleteBtn.click();
        return this;
    }

    @Step("Проверяем наличие spend с описанием: {spendingDescription}")
    public SpendingTable checkTableContainsSpending(String spendingDescription) {
        findSpendingRow(spendingDescription).shouldBe(visible);
        return this;
    }

    @Step("Проверяем количество трат в таблице: {expectedSize}")
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));
        return this;
    }

    @Step("Найти spend с описанием: {description}")
    private SelenideElement findSpendingRow(String description) {
        searchField.search(description);
        return tableRows.find(text(description));
    }
}
