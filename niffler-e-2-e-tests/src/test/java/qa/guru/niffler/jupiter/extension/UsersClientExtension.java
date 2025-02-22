package qa.guru.niffler.jupiter.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import qa.guru.niffler.api.UserApiClient;
import qa.guru.niffler.service.UserClient;
import qa.guru.niffler.service.impl.database.UserDbClient;

import java.lang.reflect.Field;

public class UsersClientExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field declaredField : testInstance.getClass().getDeclaredFields()) {
            if (declaredField.getType().isAssignableFrom(UserClient.class)) {
                declaredField.setAccessible(true);
                declaredField.set(testInstance, "api".equals(System.getProperty("client.env"))
                        ? new UserApiClient()
                        : new UserDbClient());
            }
        }

    }
}
