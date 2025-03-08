package qa.guru.niffler.test.api;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import qa.guru.niffler.service.impl.api.UserAPIClient;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Order(1)
public class FirstTest {

    UserAPIClient userAPIClient = new UserAPIClient();

    @Test
    void firstTest() {
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

}
