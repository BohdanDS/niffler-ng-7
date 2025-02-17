package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import qa.guru.niffler.utils.RandomDataUtils;

import static com.codeborne.selenide.Selenide.$;
import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;
import static qa.guru.niffler.utils.RandomDataUtils.randomSentence;

public class SpendingPage {

    private final SelenideElement amount = $("#amount"),
            category = $("#category"),
            description = $("#description"),
            saveBtn = $("#save");


    @Step("Сохранение изменений")
    public MainPage saveChange() {
        saveBtn.click();
        return new MainPage();
    }

    @Step("Заполнить поле amount")
    public SpendingPage setAmount(){
        amount.setValue("10999");
        return this;
    }

    @Step("Заполнить поле category")
    public SpendingPage setCategory(String categoryName){;
        category.setValue(categoryName);
        return this;
    }

    @Step("Заполнить поле description")
    public SpendingPage setDescription(){
        description.setValue(randomSentence(5));
        return this;
    }

}
