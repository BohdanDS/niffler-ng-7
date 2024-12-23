package qa.guru.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement
            usernameInput = $("input[name='username']"),
            passwordInput = $("input[name='password']"),
            passwordSubmitInput = $("input[name='passwordSubmit']"),
            submitBtn = $("button[type='submit']"),
            formParagraphSuccess = $(".form__paragraph_success"),
            signInButton = $(".form_sign-in"),
            errorMessage = $(".form__error");


    public RegisterPage open() {
        Selenide.open("http://127.0.0.1:9000/register");
        return this;
    }

    public RegisterPage setUsername(String username){
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    public RegisterPage setPassword(String password){
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String password){
        passwordSubmitInput.setValue(password);
        return new RegisterPage();
    }

    public LoginPage submitRegistration(){
        submitBtn.click();

        return new LoginPage();
    }

}
