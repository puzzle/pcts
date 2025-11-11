package ch.puzzle.pcts.architecture;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

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
