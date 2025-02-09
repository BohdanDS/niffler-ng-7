package qa.guru.niffler.jupiter.extention.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.annotation.Category;
import qa.guru.niffler.jupiter.annotation.Spending;
import qa.guru.niffler.jupiter.extention.CategoryExtension;
import qa.guru.niffler.jupiter.extention.SpendingExtension;
import qa.guru.niffler.jupiter.extention.UserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith({UserExtension.class, SpendingExtension.class, CategoryExtension.class})
public @interface User {
    String userName() default "";
    Category[] categories() default {};

    Spending[] spendings() default {};
}
