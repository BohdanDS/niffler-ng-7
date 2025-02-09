package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.ProfilePage;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @User(
            userName = "bohdan",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("bohdan", "123")
                .checkSuccessLogin();

        Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkArchivedCategoryExists(category[0].name());
    }

    @User(
            userName = "duck",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("bohdan", "12345")
                .checkSuccessLogin();

        Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkCategoryInCategoryList(category.name());
    }
}
