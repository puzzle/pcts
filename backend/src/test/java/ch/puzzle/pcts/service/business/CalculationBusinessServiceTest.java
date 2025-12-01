package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;
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
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private CalculationValidationService validationService;

    @Mock
    private CalculationPersistenceService persistenceService;

    @Mock
    private Calculation calculation;

    @Mock
    private Member member;

    @Mock
    private Role role;

    @InjectMocks
    private CalculationBusinessService businessService;

    @DisplayName("Should get calculation by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));

        Calculation result = businessService.getById(ID);

        assertEquals(calculation, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should throw error when calculation with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(ID));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(
                List.of(Map.of(FieldKey.FIELD, "id",
                        FieldKey.IS, ID.toString(),
                        FieldKey.ENTITY, CALCULATION)),
                exception.getErrorAttributes()
        );

        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should create calculation")
    @Test
    void shouldCreate() {
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.create(calculation);

        assertEquals(calculation, result);
        verify(validationService).validateOnCreate(calculation);
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should set publication fields on create when state is ACTIVE and archive old calculation")
    @Test
    void shouldSetPublicationFieldsOnCreateWhenStateActive() {
        mockActiveCalculation();
        when(persistenceService.save(calculation)).thenReturn(calculation);
        when(persistenceService.getAllByMemberIdAndRoleIdAndState(1L, 1L, CalculationState.ACTIVE))
                .thenReturn(List.of(calculation));

        businessService.create(calculation);

        verifyPublicationFieldsSet();
        verify(persistenceService, times(2)).save(calculation);
    }

    @DisplayName("Should set publication fields on update when state is ACTIVE and archive previous active calculation")
    @Test
    void shouldSetPublicationFieldsOnUpdateWhenStateActiveAndArchiveOld() {
        mockActiveCalculation();
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));
        when(persistenceService.save(calculation)).thenReturn(calculation);
        when(persistenceService.getAllByMemberIdAndRoleIdAndState(1L, 1L, CalculationState.ACTIVE))
                .thenReturn(List.of(calculation));
        when(calculation.getId()).thenReturn(2L);

        businessService.update(ID, calculation);

        verifyPublicationFieldsSet();
        verify(persistenceService, times(2)).save(calculation);
    }

    @DisplayName("Should set publication fields on update when state is ACTIVE and not archive old calculation if same id")
    @Test
    void shouldSetPublicationFieldsOnUpdateWhenStateActiveAndNotArchiveIfSameId() {
        mockActiveCalculation();
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));
        when(persistenceService.save(calculation)).thenReturn(calculation);
        when(persistenceService.getAllByMemberIdAndRoleIdAndState(1L, 1L, CalculationState.ACTIVE))
                .thenReturn(List.of(calculation));
        when(calculation.getId()).thenReturn(ID);

        businessService.update(ID, calculation);

        verifyPublicationFieldsSet();
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should NOT set publication fields on create when state is not ACTIVE")
    @Test
    void shouldNotSetPublicationFieldsOnCreateWhenStateNotActive() {
        when(calculation.getState()).thenReturn(CalculationState.ARCHIVED);
        when(persistenceService.save(calculation)).thenReturn(calculation);

        businessService.create(calculation);

        verify(calculation, never()).setPublicationDate(any());
        verify(calculation, never()).setPublicizedBy(any());
    }

    @DisplayName("Should update calculation")
    @Test
    void shouldUpdate() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.update(ID, calculation);

        assertEquals(calculation, result);
        verify(calculation).setId(ID);
        verify(validationService).validateOnUpdate(ID, calculation);
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should NOT set publication fields on update when state is not ACTIVE")
    @Test
    void shouldNotSetPublicationFieldsOnUpdateWhenStateNotActive() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));
        when(calculation.getState()).thenReturn(CalculationState.DRAFT);
        when(persistenceService.save(calculation)).thenReturn(calculation);

        businessService.update(ID, calculation);

        verify(calculation, never()).setPublicationDate(any());
        verify(calculation, never()).setPublicizedBy(any());
    }

    @DisplayName("Should throw exception when updating non-existing calculation")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(ID, calculation));

        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }

    private void mockActiveCalculation() {
        when(calculation.getState()).thenReturn(CalculationState.ACTIVE);
        when(calculation.getMember()).thenReturn(member);
        when(member.getId()).thenReturn(1L);
        when(calculation.getRole()).thenReturn(role);
        when(role.getId()).thenReturn(1L);
    }

    private void verifyPublicationFieldsSet() {
        LocalDate today = LocalDate.now();
        verify(calculation).setPublicationDate(today);
        verify(calculation).setPublicizedBy("Ldap User");
    }
}
