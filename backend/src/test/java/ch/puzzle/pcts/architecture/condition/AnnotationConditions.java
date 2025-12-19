package ch.puzzle.pcts.architecture.condition;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class AnnotationConditions {

    private AnnotationConditions() {
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> shouldBeValidDescription(String propertyName) {
        final String description = "have valid description";
        final Predicate<String> isOk = value -> !value.isBlank() && Character.isUpperCase(value.charAt(0));
        final Function<JavaAnnotation<?>, String> eventMessage = annotation -> String
                .format("The '%s' property of the '%s' annotation of '%s' does look like a valid sentence with an uppercase letter at the start and a dot at the end.",
                        propertyName,
                        annotation.getType().getName(),
                        annotation.getOwner().getDescription());

        return createCheck(propertyName, description, eventMessage, isOk);
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> haveSuffix(String propertyName, String suffix) {
        final String description = String.format("have value that ends with '%s'", suffix);
        final Predicate<String> isOk = value -> value.endsWith(suffix);
        final Function<JavaAnnotation<?>, String> eventMessage = annotation -> String
                .format("The '%s' property of the '%s' annotation of '%s' does not end with '%s'.",
                        propertyName,
                        annotation.getType().getName(),
                        annotation.getOwner().getDescription(),
                        suffix);

        return createCheck(propertyName, description, eventMessage, isOk);
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> havePrefix(String propertyName, String prefix) {
        final String description = String
                .format("have value with the name '%s' that starts with '%s'", propertyName, prefix);
        final Predicate<String> isOk = value -> value.startsWith(prefix);
        final Function<JavaAnnotation<?>, String> eventMessage = annotation -> String
                .format("The value of property '%s' of '%s' annotation of class '%s' does not start with '%s'.",
                        propertyName,
                        annotation.getType().getName(),
                        annotation.getOwner().getDescription(),
                        prefix);

        return createCheck(propertyName, description, eventMessage, isOk);
    }

    public static ArchCondition<JavaAnnotation<JavaClass>> haveValuePrefix(String prefix) {
        return havePrefix("value", prefix);
    }

    private static ArchCondition<JavaAnnotation<JavaClass>> createCheck(String propertyName, String description,
                                                                        Function<JavaAnnotation<?>, String> eventMessage,
                                                                        Predicate<String> isOk) {
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaAnnotation annotation, ConditionEvents events) {
                Optional<Object> property = annotation.get(propertyName);
                if (property.isPresent()) {
                    boolean checkPassed = switch (property.get()) {
                        case String value -> isOk.test(value);
                        case String[] values -> values.length > 0 && isOk.test(values[0]);
                        default -> throw new IllegalArgumentException();
                    };

                    if (!checkPassed) {
                        events.add(SimpleConditionEvent.violated(annotation, eventMessage.apply(annotation)));
                    }
                }
            }
        };
    }
}
