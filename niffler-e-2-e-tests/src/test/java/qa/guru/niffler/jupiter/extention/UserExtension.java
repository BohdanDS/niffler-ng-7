package qa.guru.niffler.jupiter.extention;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.TestData;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.UserClient;
import qa.guru.niffler.service.impl.api.UserAPIClient;
import qa.guru.niffler.service.impl.database.UserDbClient;
import qa.guru.niffler.utils.RandomDataUtils;

import java.util.ArrayList;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "123";

    private final UserClient usersClient = new UserAPIClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    UserJson user;
                    if ("".equals(userAnno.userName())) {
                        final String username = RandomDataUtils.randomUserName();
                        user = usersClient.createUser(username, defaultPassword);
                        System.out.println(user);
                    } else {
                        user = new UserJson(null, userAnno.userName(), null, null, null, CurrencyValues.EUR, null, null, null, new TestData(defaultPassword, null, null));
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            user.addTestData(
                                    new TestData(
                                            defaultPassword,
                                            new ArrayList<>(),
                                            new ArrayList<>()
                                    )
                            )
                    );
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(
                extensionContext.getUniqueId(),
                UserJson.class
        );
    }
}