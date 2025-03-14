package qa.guru.niffler.jupiter.extension.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.annotation.*;
import qa.guru.niffler.jupiter.extension.CategoryExtension;
import qa.guru.niffler.jupiter.extension.SpendingExtension;
import qa.guru.niffler.jupiter.extension.UserExtension;

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

    IncomeInvitation incomeInvitations() default @IncomeInvitation(count = 0);
};

//    OutcomeInvitation outcomeInvitations() default @OutcomeInvitation(count = 0)
//
//    Friend friends();

