package ch.puzzle.pcts.architecture.condition;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCodeUnit;
import com.tngtech.archunit.core.domain.JavaFieldAccess;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.apache.commons.lang3.StringUtils;

public class CodeUnitConditions {

    private CodeUnitConditions() {
    }

    public static ArchCondition<JavaCodeUnit> trimAssignedStringFields() {
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
