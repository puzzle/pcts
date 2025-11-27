package ch.puzzle.pcts.architecture.condition;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Optional;
import java.util.function.Function;

public class AnnotationConditions {

    private AnnotationConditions() {
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> haveValueSuffix(String suffix) {
        final String description = String.format("have value that ends with '%s'", suffix);
        final Function<String, Boolean> isOk = (value) -> value.endsWith(suffix);
        final Function<JavaAnnotation<?>, String> eventMessage = (annotation) -> String
                .format("The '%s' annotation of '%s' does not end with '%s'.",
                        annotation.getType().getName(),
                        annotation.getOwner().getDescription(),
                        suffix);

        return createAnnotationValueCheck(description, eventMessage, isOk);
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> haveValuePrefix(String prefix) {
        final String description = String.format("have value that starts with '%s'", prefix);
        final Function<String, Boolean> isOk = (value) -> value.startsWith(prefix);
        final Function<JavaAnnotation<?>, String> eventMessage = (annotation) -> String
                .format("The '%s' annotation of '%s' does not start with '%s'.",
                        annotation.getType().getName(),
                        annotation.getOwner().getDescription(),
                        prefix);

        return createAnnotationValueCheck(description, eventMessage, isOk);
    }

    private static ArchCondition<JavaAnnotation<JavaClass>> createAnnotationValueCheck(String description,
                                                                                       Function<JavaAnnotation<?>, String> eventMessage,
                                                                                       Function<String, Boolean> isOk) {
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaAnnotation annotation, ConditionEvents events) {
                Optional<Object> property = annotation.get("value");
                if (property.isPresent() && property.get() instanceof String[] value && value.length > 0
                    && !isOk.apply(value[0])) {
                    events.add(SimpleConditionEvent.violated(annotation, eventMessage.apply(annotation)));
                }
            }
        };
    }
}
