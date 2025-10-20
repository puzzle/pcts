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

    private final String shortName = "A";
    private final String longName = "A".repeat(251);

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

    @DisplayName("Should be successful when member is valid")
    @Test
    void validateBeanValidationWhenModelIsValid() {
        assertDoesNotThrow(() -> service.validate(getModel()));
    }

    @DisplayName("Should throw exception when name is blank")
    @Test
    void validateBeanValidationWhenNameIsBlank() {
        Member model = getModel();
        model.setName("  ");

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.name must not be blank", exception.getReason());
    }

    @DisplayName("Should throw exception when name is null")
    @Test
    void validateBeanValidationWhenNameIsNull() {
        Member model = getModel();
        model.setName(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.name must not be null", exception.getReason());
    }

    @DisplayName("Should throw exception when name is too short")
    @Test
    void validateBeanValidationWhenNameIsTooShort() {
        Member model = getModel();
        model.setName(shortName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.name size must be between 2 and 250, given " + shortName, exception.getReason());
    }

    @DisplayName("Should throw exception when name is too short")
    @Test
    void validateBeanValidationWhenNameIsTooLong() {
        Member model = getModel();
        model.setName(longName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.name size must be between 2 and 250, given " + longName, exception.getReason());
    }

    @DisplayName("Should throw exception when lastname is blank")
    @Test
    void validateBeanValidationWhenLastnameIsBlank() {
        Member model = getModel();
        model.setLastName("   ");

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.lastName must not be blank", exception.getReason());
    }

    @DisplayName("Should throw exception when lastname is null")
    @Test
    void validateBeanValidationWhenLastnameIsNull() {
        Member model = getModel();
        model.setLastName(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.lastName must not be null", exception.getReason());
    }

    @DisplayName("Should throw exception when lastname is too short")
    @Test
    void validateBeanValidationWhenLastnameIsTooShort() {
        Member model = getModel();
        model.setLastName(shortName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.lastName size must be between 2 and 250, given " + shortName, exception.getReason());
    }

    @DisplayName("Should throw exception when lastname is too long")
    @Test
    void validateBeanValidationWhenLastnameIsTooLong() {
        Member model = getModel();
        model.setLastName(longName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.lastName size must be between 2 and 250, given " + longName, exception.getReason());
    }

    @DisplayName("Should throw exception when employment state is null")
    @Test
    void validateBeanValidationWhenEmploymentStateIsNull() {
        Member model = getModel();
        model.setEmploymentState(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.employmentState must not be null", exception.getReason());
    }

    @DisplayName("Should throw exception when abbreviation is blank")
    @Test
    void validateBeanValidationWhenAbbreviationIsBlank() {
        Member model = getModel();
        model.setAbbreviation("  ");

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.abbreviation must not be blank", exception.getReason());
    }

    @DisplayName("Should throw exception when abbreviation is null")
    @Test
    void validateBeanValidationWhenAbbreviationIsNull() {
        Member model = getModel();
        model.setAbbreviation(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.abbreviation must not be null", exception.getReason());
    }

    @DisplayName("Should throw exception when abbreviation is too short")
    @Test
    void validateBeanValidationWhenAbbreviationIsShort() {
        Member model = getModel();
        model.setAbbreviation(shortName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.abbreviation size must be between 2 and 250, given " + shortName, exception.getReason());
    }

    @DisplayName("Should throw exception when abbreviation is too long")
    @Test
    void validateBeanValidationWhenAbbreviationIsLong() {
        Member model = getModel();
        model.setAbbreviation(longName);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.abbreviation size must be between 2 and 250, given " + longName, exception.getReason());
    }

    @DisplayName("Should throw exception when birth date is null")
    @Test
    void validateBeanValidationWhenBirthDateIsNull() {
        Member model = getModel();
        model.setBirthDate(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.birthDate must not be null", exception.getReason());
    }

    @DisplayName("Should throw exception when birth date is in the future")
    @Test
    void validateBeanValidationWhenBirthDateIsInTheFuture() {
        Date futureDate = Date.valueOf(LocalDate.now().plusDays(1));
        Member model = getModel();
        model.setBirthDate(futureDate);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(model));

        assertEquals("Member.birthDate must be in the past, given " + futureDate, exception.getReason());
    }

    @Override
    MemberValidationService getService() {
        return service;
    }
}
