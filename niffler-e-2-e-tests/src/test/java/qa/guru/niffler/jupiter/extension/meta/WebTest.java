package qa.guru.niffler.jupiter.extension.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extension.BrowserExtension;
import qa.guru.niffler.jupiter.extension.UsersQueueExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith({
        BrowserExtension.class,
        UsersQueueExtension.class
})
public @interface WebTest {
}
