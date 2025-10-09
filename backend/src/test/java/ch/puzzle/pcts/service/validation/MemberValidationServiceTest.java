package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class MemberValidationServiceTest {
    private AutoCloseable closeable;

    @InjectMocks
    private MemberValidationService validationService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

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
        Member member = new Member();
        member.setId(1L);

        assertDoesNotThrow(() -> validationService.validateOnUpdate(member));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when member id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenMemberIdIsNull() {
        Member member = new Member();
        member.setId(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnUpdate(member));

        assertEquals(ErrorKey.ID_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when member is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenMemberIsValid() {
        Member member = new Member();
        assertDoesNotThrow(() -> validationService.validateOnCreate(member));
    }
}
