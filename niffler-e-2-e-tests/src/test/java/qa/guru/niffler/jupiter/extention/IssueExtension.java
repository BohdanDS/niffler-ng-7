package qa.guru.niffler.jupiter.extention;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.SearchOption;
import qa.guru.niffler.api.GhApiClient;
import qa.guru.niffler.jupiter.annotation.DisabledByIssue;

public class IssueExtension implements ExecutionCondition {

    private final GhApiClient ghApiClient = new GhApiClient();

//    Создать issue и проверить

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                DisabledByIssue.class).or(() -> AnnotationSupport.findAnnotation(
                context.getRequiredTestClass(),
                DisabledByIssue.class,
                SearchOption.INCLUDE_ENCLOSING_CLASSES
        )).map(
                byIssue -> "open".equals(ghApiClient.issueState(byIssue.value()))
                        ? ConditionEvaluationResult.disabled("Disabled by issue: " + byIssue.value()) : ConditionEvaluationResult.enabled("Issue Closed")
        ).orElseGet(
                () -> ConditionEvaluationResult.enabled("Annotation @Disabled by issue not found")
        );
        return null;
    }
}