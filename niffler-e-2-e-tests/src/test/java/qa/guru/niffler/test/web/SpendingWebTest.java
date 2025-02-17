package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.jupiter.extention.meta.WebTest;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;

@WebTest
public class SpendingWebTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();

    @User(
            userName = "bohdan",
            categories = @Category(name = "", archived = true),
            spendings = @Spending(amount = 1003, category = "test-category", description = "des")
    )
    @Test
    public void changeCategoryDescriptionFromTableViewTest(SpendJson spendJson) {

        final String newDescription = "Css course";

        Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login("bohdan", "123")
                .editSpending(spendJson.description())
                .updateDescription(newDescription)
                .clickSaveBtn().
                checkUpdatedDescription(newDescription);
    }

    @User
    @Test
    void createSpendingForNewUser(UserJson user) {
        final String spendingCategory = randomCategoryName();
        loginPage.login(user.username(), user.testData().password()).checkSuccessLogin();
        mainPage.getHeader().toSpendingPage()
                .setAmount()
                .setCategory(spendingCategory)
                .saveChange().checkSpendingInTable(spendingCategory);
    }
}
