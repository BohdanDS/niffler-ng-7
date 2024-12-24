package qa.guru.niffler.jupiter;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;

public class CreateCategoryExtention implements BeforeEachCallback, AfterTestExecutionCallback {

    Faker faker = new Faker();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtention.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            faker.lorem().characters(0, 20),
                            anno.username(),
                            anno.archived()
                    );
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            spendApiClient.createCategory(category)
                    );
                    if (anno.archived()) {
                        CategoryJson storedCategory = context.getStore(CreateCategoryExtention.NAMESPACE).get(context.getUniqueId(),CategoryJson.class);

                        CategoryJson archivedCategory = new CategoryJson(
                                storedCategory.id(),
                                storedCategory.name(),
                                storedCategory.username(),
                                true
                        );
                        spendApiClient.updateCategory(archivedCategory);
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson storedCategory = context.getStore(CreateCategoryExtention.NAMESPACE).get(context.getUniqueId(),CategoryJson.class);
        if (!storedCategory.archived()){
            CategoryJson archivedCategory = new CategoryJson(
                    storedCategory.id(),
                    storedCategory.name(),
                    storedCategory.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategory);
        }
    }
}
