package qa.guru.niffler.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.CategoryExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(CategoryExtension.class)
public @interface Category {

    String username();

    boolean archived();
}