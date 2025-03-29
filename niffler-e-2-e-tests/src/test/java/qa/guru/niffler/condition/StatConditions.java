package qa.guru.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class StatConditions {
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color") {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement webElement) {
                final String rgba = webElement.getCssValue("background-color");

                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    public static WebElementsCondition color(@NotNull Color... expectedColors) {
        return new WebElementsCondition() {
            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalStateException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }
                boolean passed = true;
                List<String> actualRgbaLiat = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color expectedColor = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaLiat.add(rgba);

                    if (passed) {
                        passed = expectedColor.rgb.equals(rgba);
                    }
                }
                if (!passed) {

                    final String actualRgba = actualRgbaLiat.toString();
                    final String message = String.format("List colors mismatch (expected: %s, actual: %s", expectedRgba, actualRgba);
                    rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }
}
