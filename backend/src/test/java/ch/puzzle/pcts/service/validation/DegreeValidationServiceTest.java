package ch.puzzle.pcts.service.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DegreeValidationServiceTest extends ValidationBaseServiceTest<Degree, DegreeValidationService> {
    @InjectMocks
    private DegreeValidationService degreeValidationService;

    @Mock
    private DegreePersistenceService degreePersistenceService;

    private Long id;

    private final Date commonDate = new Date(0L);

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
                .withId(id)
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
                                       java.util.Date startDate) {
        Degree d = new Degree();
        d.setMember(member);
        d.setName(name);
        d.setInstitution("Institution");
        d.setType(degreeType);
        d.setCompleted(completed);
        d.setStartDate(startDate);
        d.setEndDate(new Timestamp(0L));
        d.setComment("Comment");
        return d;
    }

    static Stream<Arguments> invalidModelProvider() {
        Date validDate = Date.valueOf(LocalDate.of(2020, 1, 1));
        String tooLongString = new String(new char[251]).replace("\0", "x");
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");

        Member member = Member.Builder
                .builder()
                .withId(1L)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(new Timestamp(0L))
                .withBirthDate(new Timestamp(0L))
                .withOrganisationUnit(organisationUnit)
                .build();
        DegreeType type = new DegreeType(1L,
                                         "Bachelor",
                                         new BigDecimal("3.0"),
                                         new BigDecimal("2.0"),
                                         new BigDecimal("1.0"));

        return Stream
                .of(Arguments
                        .of(createDegree(null, "Computer Science", type, true, validDate),
                            "Degree.member must not be null."),
                    Arguments.of(createDegree(member, null, type, true, validDate), "Degree.name must not be null."),
                    Arguments.of(createDegree(member, "", type, true, validDate), "Degree.name must not be blank."),
                    Arguments.of(createDegree(member, "  ", type, true, validDate), "Degree.name must not be blank."),
                    Arguments
                            .of(createDegree(member, "A", type, true, validDate),
                                "Degree.name size must be between 2 and 250, given A."),
                    Arguments
                            .of(createDegree(member, tooLongString, type, true, validDate),
                                String.format("Degree.name size must be between 2 and 250, given %s.", tooLongString)),
                    Arguments
                            .of(createDegree(member, "Computer Science", null, true, validDate),
                                "Degree.type must not be null."),
                    Arguments
                            .of(createDegree(member, "Computer Science", type, null, validDate),
                                "Degree.completed must not be null."),
                    Arguments
                            .of(createDegree(member, "Computer Science", type, true, null),
                                "Degree.startDate must not be null."));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Degree degree = getValidModel();

        doNothing().when((ValidationBase<Degree>) service).validateOnCreate(any());

        service.validateOnCreate(degree);

        verify(service).validateOnCreate(degree);
        verifyNoMoreInteractions(degreePersistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Degree degree = getValidModel();

        doNothing().when((ValidationBase<Degree>) service).validateOnUpdate(anyLong(), any());

        service.validateOnUpdate(id, degree);

        verify(service).validateOnUpdate(id, degree);
        verifyNoMoreInteractions(degreePersistenceService);
    }
}