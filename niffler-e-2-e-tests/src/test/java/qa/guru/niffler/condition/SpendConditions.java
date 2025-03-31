package qa.guru.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.WebElementsCondition;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;
import qa.guru.niffler.model.SpendJson;

import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import static com.codeborne.selenide.Selenide.$$;
import static qa.guru.niffler.utils.DateFormatter.formatDate;

public class SpendConditions {

    public static WebElementsCondition spends(@NotNull List<SpendJson> spends) {
        return new WebElementsCondition() {

            private String expectedData;

            @NotNull
            @Override
            public CheckResult check(@NotNull Driver driver, @NotNull List<WebElement> elements) {

                if (spends.isEmpty()) {
                    throw new IllegalStateException("No expected bubbles given");
                }
                if (spends.size() != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", spends.size(), elements.size());
                    return rejected(message, elements);
                }
                ElementsCollection rows = $$(elements);

                for (int i = 0; i < elements.size(); i++) {
                    ElementsCollection cells = rows.get(i).$$("td");
                    System.out.println(cells.get(1).getText());
                    if (!cells.get(1).getText().equals(spends.get(i).category().name())) {
                        expectedData = spends.get(i).category().name();
                        String message = String.format(
                                "Spend category mismatch (expected: %s, actual: %s)",
                                expectedData, cells.get(1).getText()
                        );
                        return rejected(message, cells.get(1).getText());

                    }

                    if (!cells.get(2).getText().contains(Integer.toString(spends.get(i).amount().intValue()))) {
                        expectedData = spends.get(i).amount().toString();
                        String message = String.format(
                                "Spend amount mismatch (expected: %s, actual: %s)",
                                expectedData, cells.get(2).getText()
                        );
                        return rejected(message, cells.get(2).getText());

                    }
                    if (!cells.get(3).getText().equals(spends.get(i).description())) {
                        expectedData = spends.get(i).description();
                        String message = String.format(
                                "Spend description mismatch in row: %s (expected: %s, actual: %s)",
                                i, expectedData, cells.get(3).getText()
                        );
                        return rejected(message, cells.get(3).getText());

                    }

                    if (!cells.get(4).getText().equals(formatDate(spends.get(i).spendDate()))) {
                        expectedData = spends.get(i).spendDate().toString();
                        String message = String.format(
                                "Spend date mismatch in row: %s (expected: %s, actual: %s)",
                                i, expectedData, cells.get(4).getText()
                        );
                        return rejected(message, cells.get(4).getText());

                    }

                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedData;
            }

        };
    }
}
