package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;
import qa.guru.niffler.page.ProfilePage;
import qa.guru.niffler.page.components.Header;

import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;
import static qa.guru.niffler.utils.RandomDataUtils.randomName;

public class ProfileTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();

    private static final Config CFG = Config.getInstance();

    Header header = new Header();

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

    @User(userName = "Bohdan")
    @Test
    void test(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkSuccessLogin();
        header.clickOnProfileIcon();
        header.toFriendsPage();

    }

    @User
    @Test
    void editProfileTest(UserJson user) {
        final String spendingCategory = randomCategoryName();
        loginPage.login(user.username(), user.testData().password()).checkSuccessLogin();
        mainPage.getHeader().clickOnProfileIcon().toProfilePage()
                .setName(randomName())
                .setNewCategory(spendingCategory)
                .saveChanges()
                .verifySuccessPopUp();

    }
}
