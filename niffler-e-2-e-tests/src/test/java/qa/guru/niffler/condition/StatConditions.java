package qa.guru.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

public class StatConditions {
    public static WebElementCondition statBubbles(Color expectedColor) {
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

    public static WebElementsCondition statBubbles(@NotNull Bubble... expectedBubbles) {

        final ArrayList<String> expectedRgba = new ArrayList<>();
        final ArrayList<String> expectedTexts = new ArrayList<>();

        for (Bubble expectedBubble : expectedBubbles) {
            expectedRgba.add(expectedBubble.color().rgb);
            expectedTexts.add(expectedBubble.text());
        }

        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(@NotNull Driver driver, @NotNull List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalStateException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }
                boolean passed = true;
                List<String> actualRgbaList = new ArrayList<>();
                List<String> actualTextList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color expectedColor = expectedBubbles[i].color();
                    final String expectedText = expectedBubbles[i].text();
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String stringValue = elementToCheck.getText();
                    actualRgbaList.add(rgba);
                    actualTextList.add(stringValue);

                    if (passed) {
                        passed = expectedColor.rgb.equals(rgba) && stringValue.contains(expectedText);
                    }
                }
                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String message = String.format("Bubbles mismatch (expected: %s, actual: %s \n expected text: %s, actual text: %s", expectedRgba, actualRgba, expectedTexts, actualTextList);
                    return rejected(message, "Text: " + actualTextList + "\nColor: " + actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return "Text: " + expectedTexts +
                        "\nColor:" + expectedRgba;
            }
        };
    }

    public static WebElementsCondition statBubblesInAnyOrder(Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final Map<String, String> expectedBubbleMap = Arrays.stream(expectedBubbles)
                    .collect(Collectors.toMap(b -> b.color().rgb, Bubble::text, (a, b) -> a));

            @NotNull
            @Override
            public CheckResult check(@NotNull Driver driver, @NotNull List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalStateException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    String message = String.format("List size mismatch (expected: %s, actual: %s", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                Map<String, String> actualBubbleMap = new HashMap<>();
                for (WebElement element : elements) {
                    actualBubbleMap.put(element.getCssValue("background-color"), element.getText());
                }

                if (!expectedBubbleMap.equals(actualBubbleMap)) {
                    final String message = String.format("Bubbles mismatch (expected: %s, actual: %s", expectedBubbleMap, actualBubbleMap);
                    return rejected(message, actualBubbleMap.toString());
                }
                return accepted();
            }

            @Override
            public String toString() {
                return "Expected Bubbles: " + expectedBubbleMap;
            }
        };
    }
}