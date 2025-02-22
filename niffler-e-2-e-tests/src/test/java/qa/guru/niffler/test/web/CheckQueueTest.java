package qa.guru.niffler.test.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extension.UsersQueueExtension;
import qa.guru.niffler.jupiter.extension.UsersQueueExtension.*;
import qa.guru.niffler.jupiter.extension.UsersQueueExtension.UserType.Type;

@ExtendWith(UsersQueueExtension.class)
public class CheckQueueTest {
    @Test
    void test1(@UserType(Type.EMPTY) StaticUser user) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(user);
    }

    @Test
    void test2(@UserType(Type.EMPTY) StaticUser user) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(user);
    }

    @Test
    void test3(@UserType(Type.EMPTY) StaticUser user0, @UserType(Type.WITH_INCOME_REQUEST) StaticUser user1) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(user0);
        System.out.println(user1);
    }

    @Test
    void test4(@UserType(Type.WITH_FRIEND) StaticUser user) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println(user);
    }

//    @Test
//    void test5(@UserType(Type.WITH_OUTCOME_REQUEST) StaticUser user) throws InterruptedException {
//        Thread.sleep(2000);
//        System.out.println(user);
//    }
}
