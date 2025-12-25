package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberOverviewValidationServiceTest {

    @InjectMocks
    MemberOverviewValidationService validationService;

    @DisplayName("Should be successful validateOnGet() when Id is valid")
    @Test
    void validateOnGetByIdShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception validateOnGet() when Id is null")
    @Test
    void validateOnGetByIdShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }
}
