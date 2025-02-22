package qa.guru.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extension.UserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(UserExtension.class)
public @interface IncomeInvitation {
    int count();
}
