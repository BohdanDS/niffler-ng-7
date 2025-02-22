package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

    private final SelenideElement
            descriptionInput = $("#description"),
            saveBtn = $("#save");

    @Step("Обновить описание")
    public EditSpendingPage updateDescription(String newDescription){
        descriptionInput.clear();
        descriptionInput.setValue(newDescription);

        return this;
    }

    @Step("Нажать кнопку сохранить")
    public MainPage clickSaveBtn(){
        saveBtn.click();

        return new MainPage();
    }
}
