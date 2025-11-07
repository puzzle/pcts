package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeValidationServiceTest extends ValidationBaseServiceTest<Degree, DegreeValidationService> {

    @Mock
    private DegreePersistenceService degreePersistenceService;

    @InjectMocks
    private DegreeValidationService service;

    private Long id;

    private final LocalDate commonDate = LocalDate.EPOCH;

    @Override
    Degree getValidModel() {
        id = 1L;
        DegreeType degreeType = new DegreeType(id,
                                               "Degree Type 1",
                                               new BigDecimal("3.0"),
                                               new BigDecimal("2.0"),
                                               new BigDecimal("1.0"));

        OrganisationUnit organisationUnit = new OrganisationUnit(id, "/bbt");

        Member member = Member.Builder
                .builder()
                .withId(id)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        return Degree.Builder
                .builder()
                .withId(null)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution 1")
                .withCompleted(true)
                .withType(degreeType)
                .withStartDate(commonDate)
                .withEndDate(commonDate)
                .withComment("Comment 1")
                .build();
    }

    @Override
    DegreeValidationService getService() {
        return service;
    }

    private static Degree createDegree(Member member, String name, DegreeType degreeType, Boolean completed,
                                       LocalDate startDate, LocalDate endDate) {
        Degree d = new Degree();
        d.setMember(member);
        d.setName(name);
        d.setInstitution("Institution");
        d.setType(degreeType);
        d.setCompleted(completed);
        d.setStartDate(startDate);
        d.setEndDate(endDate);
        d.setComment("Comment");
        return d;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongString = new String(new char[251]).replace("\0", "x");
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        LocalDate futureDate = today.plusDays(1);

        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(pastDate)
                .withBirthDate(today)
                .withOrganisationUnit(organisationUnit)
                .build();
        DegreeType type = new DegreeType(1L,
                                         "Bachelor",
                                         new BigDecimal("3.0"),
                                         new BigDecimal("2.0"),
                                         new BigDecimal("1.0"));

        return Stream
                .of(Arguments
                        .of(createDegree(null, "Computer Science", type, true, pastDate, today),
                            "Degree.member must not be null."),
                    Arguments
                            .of(createDegree(member, null, type, true, pastDate, today),
                                "Degree.name must not be null."),
                    Arguments
                            .of(createDegree(member, "", type, true, pastDate, today),
                                "Degree.name must not be blank."),
                    Arguments
                            .of(createDegree(member, "  ", type, true, pastDate, today),
                                "Degree.name must not be blank."),
                    Arguments
                            .of(createDegree(member, "A", type, true, pastDate, today),
                                "Degree.name size must be between 2 and 250, given A."),
                    Arguments
                            .of(createDegree(member, tooLongString, type, true, pastDate, today),
                                String.format("Degree.name size must be between 2 and 250, given %s.", tooLongString)),
                    Arguments
                            .of(createDegree(member, "Computer Science", null, true, pastDate, today),
                                "Degree.type must not be null."),
                    Arguments
                            .of(createDegree(member, "Computer Science", type, null, pastDate, today),
                                "Degree.completed must not be null."),
                    Arguments
                            .of(createDegree(member, "Computer Science", type, true, null, today),
                                "Degree.startDate must not be null."),
                    Arguments
                            .of(createDegree(member, "Computer Science", type, true, futureDate, null),
                                "Degree.startDate must be in the past or present, given " + futureDate + "."));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Degree degree = getValidModel();

        DegreeValidationService degreeValidationService = spy(service);
        doNothing().when((ValidationBase<Degree>) degreeValidationService).validateOnCreate(any());

        degreeValidationService.validateOnCreate(degree);

        verify(degreeValidationService).validateOnCreate(degree);
        verifyNoMoreInteractions(degreePersistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        id = 1L;
        Degree degree = getValidModel();

        DegreeValidationService degreeValidationService = spy(service);
        doNothing().when((ValidationBase<Degree>) degreeValidationService).validateOnUpdate(anyLong(), any());

        degreeValidationService.validateOnUpdate(id, degree);

        verify(degreeValidationService).validateOnUpdate(id, degree);
        verifyNoMoreInteractions(degreePersistenceService);
    }

    @DisplayName("Should throw exception on ValidateOnUpdate and ValidateOnCreate when endDate is before startDate")
    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");
        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(today)
                .withBirthDate(pastDate)
                .withOrganisationUnit(organisationUnit)
                .build();
        DegreeType type = new DegreeType(1L,
                                         "Bachelor",
                                         new BigDecimal("3.0"),
                                         new BigDecimal("2.0"),
                                         new BigDecimal("1.0"));

        Degree degree = createDegree(member, "Degree", type, true, today, pastDate);

        List<PCTSException> exceptions = List
                .of(assertThrows(PCTSException.class, () -> service.validateOnUpdate(1L, degree)),
                    assertThrows(PCTSException.class, () -> service.validateOnCreate(degree)));

        exceptions
                .forEach(exception -> assertEquals(String
                        .format("Degree.endDate must be after the startDate, given endDate: %s and startDate: %s.",
                                pastDate,
                                today), exception.getReason()));
    }
}