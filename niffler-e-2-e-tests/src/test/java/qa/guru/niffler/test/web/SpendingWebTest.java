package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.jupiter.extention.meta.WebTest;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.page.LoginPage;

@WebTest
public class SpendingWebTest {

    @User(
            userName = "bohdan",
            categories = @Category(archived = true),
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
}
