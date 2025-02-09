package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.SpendDbClient;
import qa.guru.niffler.service.UserDbClient;

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
                                "testJDBC-1",
                                "bohdan",
                                false
                        ),
                        CurrencyValues.EUR,
                        100.0,
                        "description-JDBC-1",
                        "bohdan"
                )
        );
        System.out.println(spendJson);
    }

    static UserDbClient userDbClient = new UserDbClient();

    @ValueSource(strings = {
            "BohdanHibernate-10",
    })
    @ParameterizedTest
    void UserJdbcTest(String username) {
        UserJson userJson = userDbClient.createUser(username, "123");
        userDbClient.createIncomeInvitations(userJson, 2);
    }
//    @Test
//    void findUserById(){
//        Optional<UserEntity> userEntity = userDbClient.(UUID.fromString("1cee49df-dfb1-4017-bf2e-b286def391c5"));
//    }
}
