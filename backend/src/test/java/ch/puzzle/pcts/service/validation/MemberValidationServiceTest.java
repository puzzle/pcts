package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberValidationServiceTest {

    @InjectMocks
    private MemberValidationService validationService;

    @DisplayName("Should be successful on validateOnGetById() when id is not null")
    @Test
    void shouldBeSuccessfulOnValidateOnGetByIdWhenIdIsNotNull() {
        Long id = 1L;
        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals(ErrorKey.ID_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnDelete() when id is not null")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenIdIsNotNull() {
        Long id = 1L;
        assertDoesNotThrow(() -> validationService.validateOnDelete(id));
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnDelete(id));

        assertEquals(ErrorKey.ID_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when member id is not null")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenMemberIdIsNotNull() {
        assertDoesNotThrow(() -> validationService.validateOnUpdate(1L));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnUpdate(null));

        assertEquals(ErrorKey.ID_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when member is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenMemberIsValid() {
        Member member = new Member();
        assertDoesNotThrow(() -> validationService.validateOnCreate(member));
    }
}
