package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class UserProfileMenuComponent {
    private final SelenideElement
            profileBtn = $("a[href='/profile']");

}
