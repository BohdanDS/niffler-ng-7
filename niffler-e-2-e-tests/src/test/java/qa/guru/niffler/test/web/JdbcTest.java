package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.CurrencyValues;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.service.SpendDbClient;

import java.util.Date;

public class JdbcTest {
    @Test
    void daoTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spendJson = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "testJDBC",
                                "bohdan",
                                false
                        ),
                        CurrencyValues.EUR,
                        100.0,
                        "description-JDBC",
                        "bohdan"
                )
        );
        System.out.println(spendJson);
    }
}
