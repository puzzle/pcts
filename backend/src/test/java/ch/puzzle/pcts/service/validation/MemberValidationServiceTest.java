package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
                .withName("Member")
                .withLastName("Test")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("MT")
                .withDateOfHire(new Timestamp(0L))
                .withBirthDate(new Timestamp(0L))
                .withOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"))
                .build();
    }

    @DisplayName("Should throw exception when employment state is null")
    @Test
    void validateBeanValidationWhenEmploymentStateIsNull() {
        Member model = getModel();
        model.setEmploymentState(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.employmentState must not be null.", exception.getReason());
    }

    @DisplayName("Should throw exception when birth date is null")
    @Test
    void validateBeanValidationWhenBirthDateIsNull() {
        Member model = getModel();
        model.setBirthDate(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.birthDate must not be null.", exception.getReason());
    }

    @DisplayName("Should throw exception when birth date is in the future")
    @Test
    void validateBeanValidationWhenBirthDateIsInTheFuture() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(1));
        Member model = getModel();
        model.setBirthDate(futureDate);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.birthDate must be in the past, given " + futureDate + ".", exception.getReason());
    }

    @Override
    MemberValidationService getService() {
        return service;
    }
}
