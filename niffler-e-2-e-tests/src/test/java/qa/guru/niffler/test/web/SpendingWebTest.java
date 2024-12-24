package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.BrowserExtension;
import qa.guru.niffler.jupiter.Spending;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.page.LoginPage;

public class SpendingWebTest {

    @ExtendWith(BrowserExtension.class)
    @Spending(
            amount = 5622,
            category = "Education-test",
            description = "DEBUG",
            username = "bohdan"
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
