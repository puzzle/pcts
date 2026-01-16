package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.DEGREE;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.FieldKey;
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
import java.util.Map;
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

    private final LocalDate commonDate = LocalDate.EPOCH;

    @Override
    Degree getValidModel() {
        Long id = 1L;
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
                .withDegreeType(degreeType)
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
        d.setDegreeType(degreeType);
        d.setCompleted(completed);
        d.setStartDate(startDate);
        d.setEndDate(endDate);
        d.setComment("Comment");
        return d;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createDegree(null, "Computer Science", DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                            List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "member"))),
                    Arguments
                            .of(createDegree(MEMBER_1, null, DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegree(MEMBER_1, "", DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegree(MEMBER_1, "  ", DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createDegree(MEMBER_1, "A", DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Degree",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    "A"))),
                    Arguments
                            .of(createDegree(MEMBER_1, TOO_LONG_STRING, DEGREE_TYPE_1, true, DATE_YESTERDAY, DATE_NOW),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Degree",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createDegree(MEMBER_1, "Computer Science", null, true, DATE_YESTERDAY, DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "degreeType"))),
                    Arguments
                            .of(createDegree(MEMBER_1,
                                             "Computer Science",
                                             DEGREE_TYPE_1,
                                             null,
                                             DATE_YESTERDAY,
                                             DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "completed"))),
                    Arguments
                            .of(createDegree(MEMBER_1, "Computer Science", DEGREE_TYPE_1, true, null, DATE_NOW),
                                List.of(Map.of(FieldKey.CLASS, "Degree", FieldKey.FIELD, "startDate"))),
                    Arguments
                            .of(createDegree(MEMBER_1, "Computer Science", DEGREE_TYPE_1, true, DATE_TOMORROW, null),
                                List.of(Map.of(FieldKey.IS, "{attribute.date.past.present}"))));
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
        Degree degree = getValidModel();

        DegreeValidationService degreeValidationService = spy(service);
        doNothing().when((ValidationBase<Degree>) degreeValidationService).validateOnUpdate(anyLong(), any());

        degreeValidationService.validateOnUpdate(DEGREE_1_ID, degree);

        verify(degreeValidationService).validateOnUpdate(DEGREE_1_ID, degree);
        verifyNoMoreInteractions(degreePersistenceService);
    }

    @DisplayName("Should throw exception on ValidateOnUpdate and ValidateOnCreate when endDate is before startDate")
    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        Degree degree = createDegree(MEMBER_1, "Degree", DEGREE_TYPE_1, true, DATE_NOW, DATE_YESTERDAY);

        List<PCTSException> exceptions = List
                .of(assertThrows(PCTSException.class, () -> service.validateOnUpdate(DEGREE_1_ID, degree)),
                    assertThrows(PCTSException.class, () -> service.validateOnCreate(degree)));

        exceptions
                .forEach(exception -> assertEquals(List
                        .of(Map
                                .of(FieldKey.ENTITY,
                                    DEGREE,
                                    FieldKey.FIELD,
                                    "startDate",
                                    FieldKey.IS,
                                    DATE_NOW.toString(),
                                    FieldKey.CONDITION_FIELD,
                                    "endDate",
                                    FieldKey.MAX,
                                    DATE_YESTERDAY.toString())), exception.getErrorAttributes()));
    }
}