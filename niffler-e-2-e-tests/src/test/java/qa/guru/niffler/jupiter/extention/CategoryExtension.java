package qa.guru.niffler.jupiter.extention;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import qa.guru.niffler.service.SpendDbClient;

import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(anno -> {
                    if (anno.categories().length > 0) {
                        CategoryJson category = new CategoryJson(
                                null,
                                randomCategoryName(),
                                anno.userName(),
                                anno.categories()[0].archived()
                        );
                        CategoryJson created = spendDbClient.createCategory(category);

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                created);
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson storedCategory = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (storedCategory != null) {
            spendDbClient.deleteCategory(storedCategory);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

}
