package ch.puzzle.pcts.service.validation.util;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.CalculationChild;
import java.util.List;
import java.util.Objects;

/*
* T type of the calculation child
 */
public final class CalculationChildValidationUtil {

    private CalculationChildValidationUtil() {
    }

    /**
     * Determines whether the provided calculation child has at least one other
     * entity with the same identifying attributes, excluding the case where the
     * only matching entity is itself.
     *
     * @param <T>
     *            the type of the calculation child, which must implement both
     *            {@link CalculationChild} and {@link Model}
     * @param calculationChild
     *            the entity being validated for duplicates
     * @param existingCalculationChildren
     *            a list of entities that match on the business criteria (e.g., code
     *            or identifying fields) and may include the same entity
     * @return {@code true} if another entity (with a different ID) already exists
     *         in the system; {@code false} if the only match is the same entity or
     *         if no matches exist at all
     */
    public static <T extends CalculationChild & Model> boolean validateDuplicateCalculationChildId(T calculationChild,
                                                                                                   List<T> existingCalculationChildren) {

        boolean isSameEntityOnly = existingCalculationChildren.size() == 1 && Objects
                .equals(existingCalculationChildren.getFirst().getId(), calculationChild.getId());

        return !isSameEntityOnly && !existingCalculationChildren.isEmpty();
    }
}
