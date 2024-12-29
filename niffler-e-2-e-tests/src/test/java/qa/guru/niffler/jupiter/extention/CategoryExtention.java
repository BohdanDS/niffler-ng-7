package qa.guru.niffler.jupiter.extention;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;

public class CategoryExtention implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    Faker faker = new Faker();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtention.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            faker.lorem().characters(0, 20),
                            anno.username(),
                            false
                    );
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            spendApiClient.createCategory(category)
                    );
                    if (anno.archived()) {
                        CategoryJson storedCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

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
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson storedCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!storedCategory.archived()) {
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
