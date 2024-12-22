package qa.guru.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            submitBtn = $("button[type='submit']");

    public MainPage login(String username, String password){
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitBtn.click();

        return new MainPage();
    }
}
