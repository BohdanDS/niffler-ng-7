package qa.guru.niffler.jupiter.extention;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String userName, String password, boolean empty) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
    private static final Queue<StaticUser> NOT_EMPTY_USERS = new ConcurrentLinkedDeque<>();

    static {
        EMPTY_USERS.add(new StaticUser("user1", "123", true));
        EMPTY_USERS.add(new StaticUser("user2", "123", false));
        EMPTY_USERS.add(new StaticUser("user3", "123", false));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface UserType {
        boolean empty() default true;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .findFirst()
                .map(p -> p.getAnnotation(UserType.class))
                .ifPresent(
                        userType -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch sw = StopWatch.createStarted();
                            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                                user = userType.empty()
                                        ? Optional.ofNullable(EMPTY_USERS.poll())
                                        : Optional.ofNullable(NOT_EMPTY_USERS.poll());
                            }
                            Allure.getLifecycle().updateTestCase(testCase -> {
                                testCase.setStart(new Date().getTime());
                            });

                            user.ifPresentOrElse(
                                    u -> {
                                        context.getStore(NAMESPACE)
                                                .put(context.getUniqueId(), u);
                                    },
                                    () -> new IllegalStateException("Can't find user after 30 sec")
                            );
                        }
                );

    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        StaticUser user = context.getStore(NAMESPACE).get(context.getUniqueId(), StaticUser.class);
        if (user.empty) {
            EMPTY_USERS.add(user);
        } else NOT_EMPTY_USERS.add(user);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), StaticUser.class);
    }
}
