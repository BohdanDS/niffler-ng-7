package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement
            uploadNewImageBtn = $(".css-1f8fote"),
            nameInput = $("input[id='name']"),
            saveChangesBtn = $("button[id=':r9:']"),
            categoryInput = $("#category"),
            showArchivedCheckbox = $("input[type='checkbox']");
    private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

    private final ElementsCollection categoryList = $$(".css-17u3xlq");


    public ProfilePage uploadImage(String path) {
        uploadNewImageBtn.uploadFromClasspath(path);
        return this;
    }

    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage setNewCategory(String newCategory) {
        categoryInput.setValue(newCategory);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        showArchivedCheckbox.click();
        bubblesArchived.find(text(category)).shouldBe(visible);
        return this;
    }

    public ProfilePage clickArchivedCheckbox() {
        showArchivedCheckbox.click();
        return this;
    }

    public void checkCategoryInCategoryList(String categoryName) {
        categoryList.findBy(text(categoryName)).shouldBe(visible);
    }

    public void saveChanges() {
        saveChangesBtn.click();
    }

}
