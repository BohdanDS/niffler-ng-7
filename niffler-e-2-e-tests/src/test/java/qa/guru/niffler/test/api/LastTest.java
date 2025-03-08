package qa.guru.niffler.test.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import qa.guru.niffler.service.impl.api.UserAPIClient;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Isolated
public class LastTest {

    UserAPIClient userAPIClient = new UserAPIClient();

    @Test
    void lastTest() {
        assertFalse(userAPIClient.getUsers("bohdan", null).isEmpty());
    }
}
