package qa.guru.niffler.test.web;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.condition.Color;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.annotation.ScreenShotTest;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.jupiter.extension.meta.WebTest;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.page.LoginPage;
import qa.guru.niffler.page.MainPage;
import qa.guru.niffler.page.components.StatComponent;
import qa.guru.niffler.utils.ScreenDiffResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;

@WebTest
public class SpendingWebTest {

    MainPage mainPage = new MainPage();
    LoginPage loginPage = new LoginPage();

    private static final String SPENDING_CREATED = "New spending is successfully created";

    @User(
            categories = @Category(name = "", archived = true),
            spendings = @Spending(amount = 79990, category = "test-category", description = "des")
    )
    @ScreenShotTest("/img/expected-stat.png")
    public void changeCategoryDescriptionFromTableViewTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {

        final String newDescription = "Css course";

        Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(user.testData().spends().getFirst().description())
                .updateDescription(newDescription)
                .clickSaveBtn().
                checkUpdatedDescription(newDescription);
        sleep(1000);

        BufferedImage actual = ImageIO.read(Objects.requireNonNull($("canvas[role='img']").screenshot()));

        assertFalse(new ScreenDiffResult(expected, actual));

    }


    @User(
            spendings = @Spending(amount = 79990, category = "test-category", description = "des")
    )
    @ScreenShotTest("/img/stat-updated.png")
    public void checkStatComponentAfterSpendingUpdateTests(UserJson user, BufferedImage expected) throws IOException, InterruptedException {

        final String amount = "999";

        Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(user.testData().spends().getFirst().description())
                .updateAmount(amount)
                .clickSaveBtn().
                checkUpdatedDescription(amount);
        Thread.sleep(1000);

        BufferedImage actual = ImageIO.read(Objects.requireNonNull($("canvas[role='img']").screenshot()));

        assertFalse(new ScreenDiffResult(expected, actual));

    }

    @User
    @Test
    void createSpendingForNewUser(UserJson user) {
        final String spendingCategory = randomCategoryName();
        loginPage.login(user.username(), user.testData().password()).checkSuccessLogin();
        mainPage.getHeader().toSpendingPage()
                .setAmount()
                .setCategory(spendingCategory)
                .saveChange()
                .checkAlertMessage(SPENDING_CREATED)
                .checkSpendingInTable(spendingCategory);
    }

    @User(
            spendings = {
                    @Spending(amount = 79990, category = "test-category", description = "spending-1"),
                    @Spending(amount = 79990, category = "test-category", description = "spending-2"),
                    @Spending(amount = 79990, category = "test-category", description = "spending-3")
            }
    )
    @ScreenShotTest("/img/stat-after-remove.png")
    public void checkStatComponentAfterRemovingSpendingTests(UserJson user, BufferedImage expected) throws IOException, InterruptedException {


        Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login(user.username(), user.testData().password())
                .getSpendingTable().deleteSpending((user.testData().spends().getFirst().description()));
        Thread.sleep(1000);

        BufferedImage actual = ImageIO.read(Objects.requireNonNull($("canvas[role='img']").screenshot()));

        assertFalse(new ScreenDiffResult(expected, actual));

    }

    @User(
            categories = @Category(name = "", archived = true),
            spendings = @Spending(amount = 79990, category = "test-category", description = "des")
    )
    @ScreenShotTest("/img/expected-stat.png")
    public void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException, InterruptedException {


        StatComponent statComponent = Selenide.open("http://127.0.0.1:9000/", LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();


        Thread.sleep(3000);

        assertFalse(new ScreenDiffResult(expected, statComponent.chartScreenshot()), "Screen comparison failure");

        statComponent.checkBubbles(Color.yellow);

    }
}
