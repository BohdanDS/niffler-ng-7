package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class EditSpendingPage {

    private final SelenideElement
            descriptionInput = $("#description"),
            saveBtn = $("#save");

    public EditSpendingPage updateDescription(String newDescription){
        descriptionInput.clear();
        descriptionInput.setValue(newDescription);

        return this;
    }

    public MainPage clickSaveBtn(){
        saveBtn.click();

        return new MainPage();
    }
}
