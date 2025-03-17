package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;

public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @User
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkSuccessLogin();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.login(randomUserName(), "BAD");
        loginPage.validationMessageIsDisplayed();
    }
}
