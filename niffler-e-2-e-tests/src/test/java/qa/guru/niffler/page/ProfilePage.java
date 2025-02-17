package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement
            uploadNewImageBtn = $(".css-1f8fote"),
            nameInput = $("input[id='name']"),
            saveChangesBtn = $("button[type='submit']"),
            categoryInput = $("#category"),
            popUp = $("div[role='alert']"),
            showArchivedCheckbox = $("input[type='checkbox']");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

    private final ElementsCollection categoryList = $$(".css-17u3xlq");

    private final String PROFILE_UPDATED = "Profile successfully updated";


    public ProfilePage uploadImage(String path) {
        uploadNewImageBtn.uploadFromClasspath(path);
        return this;
    }

    @Step("Ввести новое имя")
    public ProfilePage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Step("Ввести новую категорию")
    public ProfilePage setNewCategory(String newCategory) {
        categoryInput.setValue(newCategory);
        return this;
    }

    @Step("Проверка что заархивированной категории я в списке")
    public ProfilePage checkArchivedCategoryExists(String category) {
        showArchivedCheckbox.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }


    public ProfilePage clickArchivedCheckbox() {
        showArchivedCheckbox.click();
        return this;
    }

    @Step("Проверка что категория находится в списке")
    public void checkCategoryInCategoryList(String categoryName) {
        categoryList.findBy(text(categoryName)).shouldBe(visible);
    }

    @Step("Нажатие кнопки сохранить")
    public ProfilePage saveChanges() {
        saveChangesBtn.click();
        return this;
    }

    @Step("Проверка поп-апа после успешного сохранения")
    public void verifySuccessPopUp(){
        popUp.shouldBe(visible).shouldHave(text(PROFILE_UPDATED));
    }

}
