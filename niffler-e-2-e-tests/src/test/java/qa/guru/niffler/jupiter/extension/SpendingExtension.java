package qa.guru.niffler.jupiter.extension;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extension.meta.User;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.SpendJson;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.SpendClient;
import qa.guru.niffler.service.impl.api.SpendAPIClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendClient spendClient = new SpendAPIClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
                        UserJson user = context.getStore(UserExtension.NAMESPACE).get(
                                context.getUniqueId(),
                                UserJson.class
                        );

                        final String username = user != null
                                ? user.username()
                                : userAnno.userName();

                        final List<SpendJson> createdSpends = new ArrayList<>();

                        for (Spending spendAnno : userAnno.spendings()) {
                            SpendJson spend = new SpendJson(
                                    null,
                                    new Date(),
                                    new CategoryJson(
                                            null,
                                            spendAnno.category(),
                                            username,
                                            false
                                    ),
                                    spendAnno.currency(),
                                    spendAnno.amount(),
                                    spendAnno.description(),
                                    username
                            );

                            createdSpends.add(
                                    spendClient.createSpend(spend)
                            );
                        }
                        if (user != null) {
                            user.testData().spends().addAll(
                                    createdSpends
                            );
                        } else {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    createdSpends
                            );
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (SpendJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .stream()
                .toArray(SpendJson[]::new);
    }
}