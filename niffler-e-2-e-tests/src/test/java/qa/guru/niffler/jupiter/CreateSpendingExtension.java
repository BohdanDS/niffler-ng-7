package qa.guru.niffler.jupiter;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.model.CategoryJson;
import qa.guru.niffler.model.CurrencyValues;
import qa.guru.niffler.model.SpendJson;

import java.util.Date;

public class CreateSpendingExtension implements BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateSpendingExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();
    @Override
    public void beforeEach(ExtensionContext context) throws Exception{
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Spending.class)
                .ifPresent(anno -> {
                    SpendJson spend = new SpendJson(
                            null,
                            new Date(),
                            new CategoryJson(
                                    null,
                                    anno.category(),
                                    anno.username(),
                                    false

                            ),
                            CurrencyValues.EUR,
                            anno.amount(),
                            anno.description(),
                            anno.username()
                    );
                context.getStore(NAMESPACE).put(
                        context.getUniqueId(),
                        spendApiClient.createSpend(spend)
                );
                });

    }
}
