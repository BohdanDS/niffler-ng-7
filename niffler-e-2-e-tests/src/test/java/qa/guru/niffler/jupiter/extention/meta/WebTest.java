package qa.guru.niffler.jupiter.extention.meta;

import org.junit.jupiter.api.extension.ExtendWith;
import qa.guru.niffler.jupiter.extention.BrowserExtension;
import qa.guru.niffler.jupiter.extention.UsersQueueExtension;

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
