package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;

import static qa.guru.niffler.utils.RandomDataUtils.randomUserName;

public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @User(
            categories = {
                    @Category(name = "Магазины", archived = false),
                    @Category(name = "Бары", archived = true)
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "QA.GURU Advanced 7",
                            amount = 80000
                    )
            }
    )
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
