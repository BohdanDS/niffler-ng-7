package qa.guru.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;
import qa.guru.niffler.jupiter.annotation.ScreenShotTest;
import qa.guru.niffler.model.allure.ScreenDiff;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ScreenShotTestExtension implements AfterEachCallback, ParameterResolver, TestExecutionExceptionHandler {

    public static final ObjectMapper objectMapper = new ObjectMapper();


    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ScreenShotTest.class).ifPresent(
                anno -> {
                    if (anno.rewriteExpected()) {

                        File expectedFile = new File("src/test/resources/" + anno.value());
                        try {
                            ImageIO.write(getActual(), "png", expectedFile);
                        } catch (IOException e) {
                            throw new RuntimeException("Не удалось перезаписать скриншот: " + expectedFile.getAbsolutePath(), e);
                        }

                    }
                }
        );


    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return ImageIO.read(new ClassPathResource(extensionContext.getRequiredTestMethod().getAnnotation(ScreenShotTest.class).value()).getInputStream());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable.getMessage().contains("Screen comparison failure")){
            ScreenDiff screenDiff = new ScreenDiff(
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getExpected())),
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getActual())),
                    "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(getDiff()))
            );
            Allure.addAttachment(
                    "Screenshot diff",
                    "application/vnd.allure.image.diff",
                    objectMapper.writeValueAsString(screenDiff)
            );
            Allure.addAttachment(
                    "Screenshot actual",
                    "image/png",
                    new ByteArrayInputStream(imageToBytes(getActual())),
                    "png"
            );
        }
        throw throwable;
    }

    public static void setExpected(BufferedImage expected) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
    }

    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage actual) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
    }

    public static BufferedImage getActual() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage diff) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
    }

    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
