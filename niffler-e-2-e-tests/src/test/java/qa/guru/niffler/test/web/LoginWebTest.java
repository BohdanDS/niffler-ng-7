package qa.guru.niffler.test.web;


import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.config.Config;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.jupiter.extension.meta.WebTest;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;

@WebTest
public class LoginWebTest {

//    private static final String USER_NAME = "Bohdan";

    private static final Config CFG = Config.getInstance();

    @User
    @Test
    void mainPageLoginTest(UserJson user) {
        System.out.println(user);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkSuccessLogin();
    }
//    private static final String PASSWORD = "123";
////    private static final String INVALID_PASSWORD = "000";
//
//    MainPage mainPage = new MainPage();
//
//    LoginPage loginPage = new LoginPage();
//
//    @Test
//    public void successLogin() {
//        Selenide.open("http://127.0.0.1:9000/login");
//        loginPage.login(USER_NAME, PASSWORD);
//        mainPage.checkSuccessLogin();
//    }
//
//    @Test
//    public void loginWithBadCredentials(@UserType(UserType.Type.EMPTY) StaticUser user) {
//        Selenide.open("http://127.0.0.1:9000/login");
//        loginPage.login(user.username(), user.password());
//        loginPage.validationMessageIsDisplayed();
//    }

}
