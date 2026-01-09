package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

class DegreeCalculationValidationServiceTest
        extends
            ValidationBaseServiceTest<DegreeCalculation, DegreeCalculationValidationService> {

    private static final Long MEMBER_ID_1 = 1L;
    private static final Long MEMBER_ID_2 = 2L;
    private static final Long CALCULATION_ID = 1L;
    private static final Long DEGREE_CALCULATION_ID = 1L;
    private static final Long DEGREE_ID = 1L;
    private static final Long ORGANISATION_UNIT_ID = 1L;

    private static final String FIRST_NAME_1 = "Alice";
    private static final String LAST_NAME_1 = "Smith";
    private static final String FIRST_NAME_2 = "Bob";
    private static final String LAST_NAME_2 = "Johnson";
    private static final String DEGREE_NAME = "Degree 1";
    private static final String ORG_UNIT_NAME = "Org Unit";
    private static final String VALID_COMMENT = "Valid comment";
    private static final String COMMENT = "Comment";

    private static final BigDecimal VALID_WEIGHT = BigDecimal.valueOf(50);
    private static final BigDecimal NEGATIVE_WEIGHT = BigDecimal.valueOf(-1);
    private static final BigDecimal TOO_HIGH_WEIGHT = BigDecimal.valueOf(200);

    private static final String MIN_WEIGHT = "1";
    private static final String MAX_WEIGHT = "100";

    @Override
    DegreeCalculationValidationService getService() {
        return new DegreeCalculationValidationService();
    }

    @Override
    DegreeCalculation getValidModel() {
        Member member = createMember(MEMBER_ID_1, FIRST_NAME_1, LAST_NAME_1);
        Calculation calculation = createCalculation(CALCULATION_ID, member);
        Degree degree = createDegree(DEGREE_ID, member, DEGREE_NAME);

        return new DegreeCalculation(null, calculation, degree, Relevancy.HIGHLY, VALID_WEIGHT, VALID_COMMENT);
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new DegreeCalculation(null, null, new Degree(), Relevancy.HIGHLY, VALID_WEIGHT, COMMENT),
                            List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      null,
                                                      Relevancy.HIGHLY,
                                                      VALID_WEIGHT,
                                                      COMMENT),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "degree"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      null,
                                                      VALID_WEIGHT,
                                                      COMMENT),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "relevancy"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      null,
                                                      COMMENT),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "weight"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      NEGATIVE_WEIGHT,
                                                      COMMENT),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    MAX_WEIGHT,
                                                    FieldKey.MIN,
                                                    MIN_WEIGHT,
                                                    FieldKey.IS,
                                                    NEGATIVE_WEIGHT.toString()))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      TOO_HIGH_WEIGHT,
                                                      COMMENT),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    MAX_WEIGHT,
                                                    FieldKey.MIN,
                                                    MIN_WEIGHT,
                                                    FieldKey.IS,
                                                    TOO_HIGH_WEIGHT.toString()))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        DegreeCalculationValidationService spyService = spy(getService());

        Member member1 = createMember(MEMBER_ID_1, FIRST_NAME_1, LAST_NAME_1);
        Member member2 = createMember(MEMBER_ID_2, FIRST_NAME_2, LAST_NAME_2);

        Degree degree = createDegree(DEGREE_ID, member1, DEGREE_NAME);
        DegreeCalculation dc = new DegreeCalculation();
        dc.setCalculation(createCalculationWithMember(member2));
        dc.setDegree(degree);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(dc));

        assertEquals(ErrorKey.ATTRIBUTE_MATCHES, exception.getErrorKeys().getFirst());
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should throw exception on duplicate degree ID")
    @Test
    void shouldThrowExceptionOnDuplicateDegreeId() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();
        DegreeCalculation existingDc = getValidModel();
        existingDc.setId(DEGREE_CALCULATION_ID);

        List<DegreeCalculation> existing = List.of(existingDc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateDegreeId(dc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().getFirst());
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.IS, DEGREE_NAME),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should not throw exception when only same entity exists")
    @Test
    void shouldNotThrowWhenOnlySameEntityExists() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();
        dc.setId(CALCULATION_ID);

        List<DegreeCalculation> existing = List.of(dc);

        assertDoesNotThrow(() -> spyService.validateDuplicateDegreeId(dc, existing));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(dc);

        spyService.validateOnCreate(dc);

        verify(spyService).validateMemberForCalculation(dc);
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member member = new Member();
        member.setId(id);
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setBirthDate(LocalDate.of(1980, 1, 1));
        member.setOrganisationUnit(new OrganisationUnit(ORGANISATION_UNIT_ID, ORG_UNIT_NAME));
        return member;
    }

    private Degree createDegree(Long id, Member member, String name) {
        Degree degree = new Degree();
        degree.setId(id);
        degree.setMember(member);
        degree.setName(name);
        return degree;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calculation = new Calculation();
        calculation.setId(id);
        calculation.setMember(member);
        return calculation;
    }

    private Calculation createCalculationWithMember(Member member) {
        Calculation calculation = new Calculation();
        calculation.setMember(member);
        return calculation;
    }
}
