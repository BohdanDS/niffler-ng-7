package qa.guru.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.data.entity.userdata.CurrencyValues;
import qa.guru.niffler.jupiter.extension.SpendingExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(SpendingExtension.class)
public @interface Spending {
    double amount();

    String category();

    String description();

    CurrencyValues currency() default CurrencyValues.EUR;

}
