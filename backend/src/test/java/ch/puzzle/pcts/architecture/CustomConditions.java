package ch.puzzle.pcts.architecture;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

public class CustomConditions {

    public static final ArchCondition<JavaClass> overrideEqualsMethod = createMethodOverrideCondition("equals",
                                                                                                      Object.class);

    public static final ArchCondition<JavaClass> overrideToStringMethod = createMethodOverrideCondition("toString");

    public static final ArchCondition<JavaClass> overrideHashCodeMethod = createMethodOverrideCondition("hashCode");

    public static ArchCondition<JavaPackage> followPattern(String pattern) {
        String description = String.format("follow pattern '%s'", pattern);
        return new ArchCondition<>(description) {
            @Override
            public void check(JavaPackage javaPackage, ConditionEvents events) {
                String packageName = javaPackage.getName();

                if (!packageName.matches(pattern)) {
                    String message = String
                            .format("Package '%s' does not follow pattern '%s'", javaPackage.getName(), pattern);
                    events.add(SimpleConditionEvent.violated(javaPackage, message));
                }
            }
        };
    }

    private static ArchCondition<JavaClass> createMethodOverrideCondition(String methodName, Class<?>... parameters) {
        return new ArchCondition<>("override %s()".formatted(methodName)) {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                Optional<JavaMethod> method = javaClass.tryGetMethod(methodName, parameters);
                boolean isOverridden = method.isPresent() && !method.get().getOwner().isEquivalentTo(Object.class);

                if (!isOverridden) {
                    String message = String
                            .format("Class %s in %s does not override %s()",
                                    javaClass.getName(),
                                    javaClass.getSourceCodeLocation(),
                                    methodName);
                    events.add(SimpleConditionEvent.violated(javaClass, message));
                }
            }
        };
    }

    private static ArchCondition<JavaClass> createAnnotationCheckMethod(Class<?> annotationClass,
                                                                        Function<JavaAnnotation<?>, Boolean> isOk,
                                                                        Class<?>... exceptions) {
        return new ArchCondition<>("should have a @RequestMapping annotation with a value that ends with 's'") {

            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                List<JavaAnnotation<JavaClass>> annotations = javaClass
                        .getAnnotations()
                        .stream()
                        .filter(a -> a.getRawType().isAssignableTo(annotationClass))
                        .filter(a -> {
                            for (Class<?> exception : exceptions) {
                                if (javaClass.isAssignableTo(exception)) {
                                    return false;
                                }
                            }
                            return true;
                        })
                        .toList();

                for (JavaAnnotation<?> annotation : annotations) {
                    boolean checkPassed = isOk.apply(annotation);
                    if (!checkPassed) {
                        String message = String
                                .format("The @RequestMapping annotation of '%s' does not end with 's', which means it is possible it is not plural",
                                        javaClass.getName());
                        events.add(SimpleConditionEvent.violated(javaClass, message));
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> havePluralEndpointName(Class<?>... exceptions) {
        Function<JavaAnnotation<?>, Boolean> isOk = (annotation) -> {
            Optional<Object> property = annotation.get("value");
            if (property.isPresent() && property.get() instanceof String[] value && value.length > 0) {
                return value[0].endsWith("s");
            }
            return true;
        };

        return createAnnotationCheckMethod(RequestMapping.class, isOk, exceptions);
    }

    public static ArchCondition<JavaClass> startWithApiPrefix(Class<?>... exceptions) {
        Function<JavaAnnotation<?>, Boolean> isOk = (annotation) -> {
            Optional<Object> property = annotation.get("value");
            if (property.isPresent() && property.get() instanceof String[] value && value.length > 0) {
                return value[0].startsWith("/api/v1/");
            }
            return true;
        };

        return createAnnotationCheckMethod(RequestMapping.class, isOk, exceptions);
    }

    static ArchCondition<JavaCodeUnit> trimAssignedStringFields() {
        return new ArchCondition<>("use StringUtils.trim() before assigning to String fields") {
            @Override
            public void check(JavaCodeUnit codeUnit, ConditionEvents events) {
                // Track all fields being set and only take the set accesses
                for (JavaFieldAccess fieldAccess : codeUnit.getFieldAccesses()) {
                    if (fieldAccess.getAccessType() != JavaFieldAccess.AccessType.SET) {
                        continue;
                    }

                    AccessTarget.FieldAccessTarget targetField = fieldAccess.getTarget();
                    if (!targetField.getRawType().isEquivalentTo(String.class)) {
                        continue;
                    }

                    boolean trimsValue = codeUnit
                            .getMethodCallsFromSelf()
                            .stream()
                            .anyMatch(call -> call.getTargetOwner().isEquivalentTo(StringUtils.class)
                                              && call.getName().equals("trim"));

                    if (!trimsValue) {
                        events
                                .add(SimpleConditionEvent
                                        .violated(codeUnit,
                                                  codeUnit.getFullName() + " assigns a String field '"
                                                            + targetField.getName()
                                                            + "' without calling StringUtils.trim()"));
                    }
                }
            }
        };
    }
}
