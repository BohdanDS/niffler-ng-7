package qa.guru.niffler.test.web;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.jupiter.extention.meta.WebTest;
import qa.guru.niffler.page.RegisterPage;

@WebTest
@DisplayName("Registrations Web tests")
public class RegistrationWebTest {

    static final Faker faker = new Faker();
    private static final String USER_NAME = faker.name().username();
    private static final String EXISTING_USER = "Bohdan";
    private static final String PASSWORD = "123";

    @Test
    public void newUserSuccessRegistration(){
        new RegisterPage()
                .open()
                .setUsername(USER_NAME)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .submitRegistration()
                .checkSuccessfulRegistrationMessage();

    }
    @Test
    public void registrationWithExistingUsername(){
        new RegisterPage()
                .open()
                .setUsername(EXISTING_USER)
                .setPassword(PASSWORD)
                .setPasswordSubmit(PASSWORD)
                .submitRegistration()
                .validationMessageIsDisplayed();
    }

    @Test
    public void registrationWithDifferentPasswords(){
        new RegisterPage()
                .open()
                .setUsername(EXISTING_USER)
                .setPassword(PASSWORD)
                .setPasswordSubmit(faker.internet().password())
                .submitRegistration()
                .validationMessageIsDisplayed();
    }

}
