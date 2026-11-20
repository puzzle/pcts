package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationValidationServiceTest extends ValidationBaseServiceTest<Calculation, CalculationValidationService> {

    @Mock
    private CalculationPersistenceService persistenceService;

    @InjectMocks
    private CalculationValidationService service;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        Calculation defaultOldModelInDb = getValidModel();
        lenient().when(persistenceService.getById(GENERIC_1_ID)).thenReturn(defaultOldModelInDb);
    }

    @Override
    Calculation getValidModel() {
        Calculation calculation = new Calculation();
        calculation.setMember(MEMBER_1);
        calculation.setRole(ROLE_2);
        calculation.setState(CalculationState.ACTIVE);
        calculation.setPublicationDate(DATE_NOW);
        calculation.setPublicizedBy(VALID_STRING);
        return calculation;
    }

    @Override
    CalculationValidationService getService() {
        return service;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createCalculation(null, ROLE_2, CalculationState.ACTIVE),
                            List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "member"))),
                    Arguments
                            .of(createCalculation(MEMBER_1, null, CalculationState.ACTIVE),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "role"))),
                    Arguments
                            .of(createCalculation(MEMBER_1, ROLE_2, null),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "state"))));
    }

    private static Calculation createCalculation(Member member, Role role, CalculationState state) {
        Calculation calculation = new Calculation();
        calculation.setMember(member);
        calculation.setRole(role);
        calculation.setState(state);
        calculation.setPublicationDate(DATE_NOW);
        calculation.setPublicizedBy(VALID_STRING);
        return calculation;
    }

    @DisplayName("Should throw if an archived calculation is edited")
    @Test
    void validateOnUpdateShouldThrowIfArchived() {
        Calculation calculationToUpdate = getValidModel();
        calculationToUpdate.setId(GENERIC_1_ID);

        Calculation oldCalculationInDb = getValidModel();
        oldCalculationInDb.setState(CalculationState.ARCHIVED);

        when(persistenceService.getById(GENERIC_1_ID)).thenReturn(oldCalculationInDb);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(GENERIC_1_ID, calculationToUpdate));

        assertEquals(List.of(ErrorKey.ARCHIVED_CALCULATION_NOT_EDITABLE), exception.getErrorKeys());

        Map<FieldKey, String> expectedAttributes = Map
                .of(FieldKey.ENTITY,
                    CALCULATION,
                    FieldKey.FIELD,
                    "state",
                    FieldKey.IS,
                    CalculationState.ARCHIVED.name());
        assertEquals(List.of(expectedAttributes), exception.getErrorAttributes());
    }

    @DisplayName("Should pass validateOnUpdate if calculation is not archived")
    @Test
    void validateOnUpdateShouldPassIfNotArchived() {
        Calculation calculationToUpdate = getValidModel();
        calculationToUpdate.setId(GENERIC_1_ID);

        Calculation oldCalculationInDb = getValidModel();
        oldCalculationInDb.setState(CalculationState.ACTIVE);

        when(persistenceService.getById(GENERIC_1_ID)).thenReturn(oldCalculationInDb);

        assertDoesNotThrow(() -> service.validateOnUpdate(GENERIC_1_ID, calculationToUpdate));
        verify(persistenceService).getById(GENERIC_1_ID);
    }
}