package qa.guru.niffler.jupiter.extention;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.extention.meta.User;
import qa.guru.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import qa.guru.niffler.model.UserJson;
import qa.guru.niffler.service.SpendClient;
import qa.guru.niffler.service.SpendDbClient;

import java.util.ArrayList;
import java.util.List;

import static qa.guru.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendClient spendClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.categories())) {
                        UserJson user = context.getStore(UserExtension.NAMESPACE).get(
                                context.getUniqueId(),
                                UserJson.class
                        );

                        final String username = user != null
                                ? user.username()
                                : userAnno.userName();

                        final List<CategoryJson> createdCategories = new ArrayList<>();

                        for (Category categoryAnno : userAnno.categories()) {
                            CategoryJson category = new CategoryJson(
                                    null,
                                    "".equals(categoryAnno.name()) ? randomCategoryName() : categoryAnno.name(),
                                    username,
                                    categoryAnno.archived()
                            );
                            createdCategories.add(
                                    spendClient.createCategory(category)
                            );
                        }
                        if (user != null) {
                            user.testData().categories().addAll(
                                    createdCategories
                            );
                        } else {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    createdCategories
                            );
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .stream()
                .toArray(CategoryJson[]::new);
    }
}
