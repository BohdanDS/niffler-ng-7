package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;
import qa.guru.niffler.config.Config;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;


public class LoginPage {

    private static final Config instance = Config.getInstance();

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitBtn = $("button[type='submit']"),
            errorMessage = $(".form__error");

    public MainPage login(String username, String password){
        System.out.println("123");
//        Selenide.open(instance.frontUrl());
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitBtn.click();

        return new MainPage();
    }

    public void validationMessageIsDisplayed() {
        errorMessage.shouldBe(visible);
    }

}
