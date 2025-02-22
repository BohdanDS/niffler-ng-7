package qa.guru.niffler.jupiter.extention.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.IncomeExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtendWith(IncomeExtension.class)
public @interface IncomeRequest {

    int count() default 0;

    List<String> incomeRequestUser = new ArrayList<>();
}
