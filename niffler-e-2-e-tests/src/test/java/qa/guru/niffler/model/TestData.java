package qa.guru.niffler.model;

import java.util.List;

public record TestData(String password, List<CategoryJson> categories, List<SpendJson> spends) {
}
