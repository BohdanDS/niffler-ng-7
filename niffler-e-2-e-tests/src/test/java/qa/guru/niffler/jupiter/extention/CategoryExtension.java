package qa.guru.niffler.jupiter.extention;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.api.SpendApiClient;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;

import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    Faker faker = new Faker();

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        CategoryJson category = new CategoryJson(
                                null,
                                randomCategoryName(),
                                anno.userName(),
                                false
                        );
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spendApiClient.createCategory(category)
                        );
                        if (category.archived()) {
                            CategoryJson storedCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

                            CategoryJson archivedCategory = new CategoryJson(
                                    storedCategory.id(),
                                    storedCategory.name(),
                                    storedCategory.username(),
                                    true
                            );
                            spendApiClient.updateCategory(archivedCategory);
                        }
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
