package qa.guru.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

    public static final String caseName = "Niffler Backend logs";

    @SneakyThrows
    @Override
    public void afterSuit() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));

        allureLifecycle.startTestCase(caseId);

        attachServiceLog(allureLifecycle, "Niffler-auth log", "./logs/niffler-auth/app.log");
        attachServiceLog(allureLifecycle, "Niffler-currency log", "./logs/niffler-currency/app.log");
        attachServiceLog(allureLifecycle, "Niffler-spend log", "./logs/niffler-spend/app.log");
        attachServiceLog(allureLifecycle, "Niffler-userdata log", "./logs/niffler-userdata/app.log");


        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

//    Нужно протестировать
    private void attachServiceLog(AllureLifecycle allureLifecycle, String serviceName, String servicePath) throws IOException {
        allureLifecycle.addAttachment(
                serviceName,
                "text/html",
                ".log",
                Files.newInputStream(
                        Path.of(servicePath)
                )
        );
    }
}
