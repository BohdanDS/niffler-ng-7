package qa.guru.niffler.jupiter.extention;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.jupiter.extention.meta.IncomeRequest;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.UserClient;
import qa.guru.niffler.service.impl.api.UserAPIClient;

import java.util.List;

public class IncomeExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(IncomeExtension.class);
    private final UserClient usersClient = new UserAPIClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), IncomeRequest.class)
                .ifPresent(anno -> {
                    UserJson user = context.getStore(UserExtension.NAMESPACE).get(
                            context.getUniqueId(),
                            UserJson.class
                    );
                    List<UserJson> list = usersClient.createIncomeInvitations(user, anno.count());

                    List<String> userNames = list.stream().map(UserJson::username).toList();

                    context.getStore(NAMESPACE).put(context.getUniqueId(), userNames);
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(List.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(
                extensionContext.getUniqueId(),
                List.class
        );
    }
}
