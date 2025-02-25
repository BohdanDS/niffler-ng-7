package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static com.codeborne.selenide.SetValueOptions.withDate;

public class Calendar extends BaseComponent<Calendar> {

    private static final String DATE_INPUT = "input[name='date']";

    protected Calendar(SelenideElement self) {
        super(self);
    }


    @Step("Установить дату в календаре")
    public Calendar setDate(Date date) {
        String dateFormat = Optional.ofNullable(
                self.$(DATE_INPUT).getAttribute("value")
        ).orElse("MM/DD/YYYY");
        LocalDate spendingDate = LocalDate.parse(date.toString(),
                DateTimeFormatter.ofPattern(dateFormat));
        self.$(DATE_INPUT).setValue(
                withDate(spendingDate)
        );
        return this;
    }

}
