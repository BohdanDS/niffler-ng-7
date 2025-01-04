package qa.guru.niffler.jupiter.extention;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.*;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income_requests,
            String outcome_requests) {
    }


    private final static Queue<StaticUser> EMPTY_USER = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_FRIEND = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_INCOME_REQUEST = new ConcurrentLinkedQueue<>();
    private final static Queue<StaticUser> USERS_WITH_OUTCOME_REQUEST = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USER.add(new StaticUser("EMPTY_USER", "123", null, null, null));
        USERS_WITH_FRIEND.add(new StaticUser("USER_WITH_FRIEND", "123", "bohdan", null, null));
        USERS_WITH_INCOME_REQUEST.add(new StaticUser("USER_INCOME_REQUEST", "123", null, "bohdan-INCOME_REQUEST", null));
        USERS_WITH_OUTCOME_REQUEST.add(new StaticUser("USER_OUTCOME_REQUEST", "123", null, null, "bohdan"));
    }

    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USER;
            case WITH_FRIEND -> USERS_WITH_FRIEND;
            case WITH_INCOME_REQUEST -> USERS_WITH_INCOME_REQUEST;
            case WITH_OUTCOME_REQUEST -> USERS_WITH_OUTCOME_REQUEST;
        };
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Map<UserType, StaticUser> usersMap = new HashMap<>();

        List<Parameter> parameters = Arrays.stream(context.getRequiredTestMethod().getParameters()).filter(parameter -> AnnotationSupport.isAnnotated(parameter, UserType.class)).toList();

        parameters.forEach(parameter -> {
            UserType userType = parameter.getAnnotation(UserType.class);
            Queue<StaticUser> queue = getQueueByUserType(userType.value());

            Optional<StaticUser> user = Optional.empty();
            StopWatch sw = StopWatch.createStarted();

            while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                user = Optional.ofNullable(queue.poll());
            }

            user.ifPresentOrElse(u -> usersMap.put(userType, u), () -> {
                throw new IllegalStateException("User wasn'y found");
            });

        });

        context.getStore(NAMESPACE).put(context.getUniqueId(), usersMap);

        Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));

    }


    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        Map<UserType, StaticUser> usersfromStoreMap = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
            if (usersfromStoreMap != null){
                for (Map.Entry<UserType, StaticUser> e : usersfromStoreMap.entrySet()) {
                    StaticUser user = e.getValue();
                    UserType userType = e.getKey();

                    Queue<StaticUser> queue = getQueueByUserType(userType.value());
                    queue.add(user);
                }
            }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class) && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return map.get(parameterContext.getParameter().getAnnotation(UserType.class));
    }
}
