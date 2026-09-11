package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.CalculationChildInterface;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.validation.util.CalculationChildValidationUtil;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * This base class contains the validations shared by all calculation children
 *
 * @param <T>
 *            the type of the calculation child extending
 *            {@link CalculationChildInterface} and {@link Model}
 */
@Service
public abstract class CalculationChildValidationBase<T extends CalculationChildInterface & Model>
        extends
            ValidationBase<T> {

    protected void validateMemberMatchesCalculation(T calculationChild, String childField,
                                                    Function<T, Member> childMemberGetter) {
        Member childMember = childMemberGetter.apply(calculationChild);
        Member calculationMember = calculationChild.getCalculation().getMember();

        if (!childMember.equals(calculationMember)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, childField, FieldKey.CONDITION_FIELD, MEMBER);

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    protected void validateNoDuplicate(T calculationChild, List<T> existingCalculationChildren, String childField,
                                       Function<T, String> nameGetter) {
        if (CalculationChildValidationUtil
                .validateDuplicateCalculationChildId(calculationChild, existingCalculationChildren)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        childField,
                        FieldKey.IS,
                        nameGetter.apply(calculationChild));

            GenericErrorDto error = new GenericErrorDto(ErrorKey.DUPLICATE_CALCULATION, attributes);
            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
