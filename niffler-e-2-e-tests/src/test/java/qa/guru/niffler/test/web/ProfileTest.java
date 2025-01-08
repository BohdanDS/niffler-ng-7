package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.page.LoginPage;

public class ProfileTest {

    @User(
            userName = "bohdan",
            categories = @Category(archived = true)
    )
    @Test
    public void archivedCategoryPresentsInList(CategoryJson category) {

        Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login("bohdan", "123")
                .checkSuccessLogin();

    }
}
