package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement
            descriptionInput = $("#description"),
            amountInput = $("#amount"),
            saveBtn = $("#save");

    @Step("Обновить описание")
    public EditSpendingPage updateDescription(String newDescription){
        descriptionInput.clear();
        descriptionInput.setValue(newDescription);

        return this;
    }
    @Step("Обновить сумму")
    public EditSpendingPage updateAmount(String amount){
        amountInput.clear();
        amountInput.setValue(amount);

        return this;
    }

    @Step("Нажать кнопку сохранить")
    public MainPage clickSaveBtn(){
        saveBtn.click();

        return new MainPage();
    }

    @Override
    public EditSpendingPage checkThatPageLoaded() {
        return null;
    }
}
