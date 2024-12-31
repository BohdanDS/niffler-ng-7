package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class HeaderComponent {
    private final SelenideElement newSpendingBtn = $(".css-1v1p78s"),
            userProfileMenu = $(".MuiAvatar-root");

    public void clickOnProfileIcon() {
        userProfileMenu.click();
    }
}
