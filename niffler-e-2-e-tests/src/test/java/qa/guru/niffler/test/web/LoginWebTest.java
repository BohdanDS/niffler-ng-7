package qa.guru.niffler.test.web;


import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.BrowserExtension;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;

@ExtendWith(BrowserExtension.class)
public class LoginWebTest {

    private static final String USER_NAME = "Bohdan";
    private static final String PASSWORD = "123";
    private static final String INVALID_PASSWORD = "000";

    MainPage mainPage = new MainPage();

    LoginPage loginPage = new LoginPage();

    @Test
    public void successLogin(){
        Selenide.open("http://127.0.0.1:9000/login");
        loginPage.login(USER_NAME, PASSWORD);
        mainPage.checkSuccessLogin();
    }

    @Test
    public void loginWithBadCredentials(){
        Selenide.open("http://127.0.0.1:9000/login");
        loginPage.login(USER_NAME, INVALID_PASSWORD);
        loginPage.validationMessageIsDisplayed();

    }

}
