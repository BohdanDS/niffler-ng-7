package qa.guru.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import lombok.Getter;
import qa.guru.niffler.page.components.Search;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {

    @Getter
    private final Search search = new Search();
    private static final String WAITING = "Waiting...";

    private final ElementsCollection allPeopleRows = $$("#all tr");

    @Step("Подтвердить что отображаются заявки на добавления в друзья")
    public void verifyOutcomeInvitations(List<String> names){
        for (String name : names) {
            allPeopleRows.findBy(text(name)).$("p").shouldHave(text(name)).shouldBe(visible);
            allPeopleRows.findBy(text(name)).$("span").shouldHave(text(WAITING)).shouldBe(visible);
        }
    }
}
