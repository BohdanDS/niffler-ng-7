package qa.guru.niffler.jupiter.extention;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.CurrencyValues;
import qa.guru.niffler.model.SpendJson;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.spendings().length > 0) {
                        Spending annoSpending = anno.spendings()[0];
                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                new CategoryJson(
                                        null,
                                        annoSpending.category(),
                                        anno.userName(),
                                        false

                                ),
                                CurrencyValues.EUR,
                                annoSpending.amount(),
                                annoSpending.description(),
                                anno.userName()
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendApiClient.addSpend(spend)
                        );
                    }
                });

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
