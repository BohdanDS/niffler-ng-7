package qa.guru.niffler.page.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Selenide.$;

public class Search extends BaseComponent<Search> {
    private final SelenideElement self = $("input[aria-label='search']");

    protected Search(SelenideElement self) {
        super(self);
    }

    public Search() {
        super($("input[aria-label='search']"));
    }

    public void search(String search) {
        self.clear();
        self.setValue(search).pressEnter();
    }

    public Search clearIfNotEmpty() {
        if (!self.is(empty)) {
            self.clear();
        }
        return this;
    }

}
