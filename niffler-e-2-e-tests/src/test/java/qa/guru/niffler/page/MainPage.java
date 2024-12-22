package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$("td",5).click();

        return new EditSpendingPage();
    }

    public void checkUpdatedDescription(String updatedDescription){
        tableRows.find(text(updatedDescription)).shouldBe(visible);
    }
}
