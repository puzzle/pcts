package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.sql.Timestamp;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberValidationServiceTest extends ValidationBaseServiceTest<Member, MemberValidationService> {

    @InjectMocks
    MemberValidationService service;

    @Override
    Member getModel() {
        return Member.Builder
                .builder()
                .withId(null)
                .withName("Member 3")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("M1")
                .withDateOfHire(new Timestamp(0L))
                .withBirthDate(new Timestamp(0L))
                .withOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"))
                .build();
    }

    @Override
    void validate() {
    }

    @Override
    MemberValidationService getService() {
        return service;
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
        assertDoesNotThrow(() -> service.validateOnDelete(id));
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
        assertDoesNotThrow(() -> service.validateOnUpdate(1L));
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
        assertDoesNotThrow(() -> service.validateOnCreate(member));
    }
}
