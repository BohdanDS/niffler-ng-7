package qa.guru.niffler.test.api;

import org.junit.jupiter.api.Test;
import qa.guru.niffler.service.impl.api.UserAPIClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsersApiTest {

    UserAPIClient userAPIClient = new UserAPIClient();
    @Test
    void emptyListTest(){
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

    @Test
    void emptyListTest1(){
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

    @Test
    void emptyListTest2(){
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

    @Test
    void emptyListTest3(){
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

    @Test
    void emptyListTest4(){
        assertTrue(userAPIClient.getUsers("getEmptyResult", "null").isEmpty());
    }

    @Test
    void notEmptyListTest(){
        assertFalse(userAPIClient.getUsers("bohdan", null).isEmpty());
    }
}
