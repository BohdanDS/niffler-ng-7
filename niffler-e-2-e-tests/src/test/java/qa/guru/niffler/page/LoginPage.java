package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPage {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitBtn = $("button[type='submit']"),
            errorMessage = $(".form__error");

    public MainPage login(String username, String password){
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitBtn.click();

        return new MainPage();
    }

    public void validationMessageIsDisplayed() {
        errorMessage.shouldBe(visible);
    }

}
