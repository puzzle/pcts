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

    @Override
    DegreeCalculationValidationService getService() {
        return new DegreeCalculationValidationService();
    }

    @Override
    DegreeCalculation getValidModel() {
        Member member = createMember(1L, "Alice", "Smith");
        Calculation calculation = createCalculation(1L, member);
        Degree degree = createDegree(1L, member, "Degree 1");

        return new DegreeCalculation(null,
                                     calculation,
                                     degree,
                                     Relevancy.HIGHLY,
                                     BigDecimal.valueOf(50),
                                     "Valid comment");
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new DegreeCalculation(null,
                                                  null,
                                                  new Degree(),
                                                  Relevancy.HIGHLY,
                                                  BigDecimal.valueOf(50),
                                                  "Comment"),
                            List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      null,
                                                      Relevancy.HIGHLY,
                                                      BigDecimal.valueOf(50),
                                                      "Comment"),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "degree"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      null,
                                                      BigDecimal.valueOf(50),
                                                      "Comment"),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "relevancy"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      null,
                                                      "Comment"),
                                List.of(Map.of(FieldKey.CLASS, "DegreeCalculation", FieldKey.FIELD, "weight"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      BigDecimal.valueOf(-1),
                                                      "Comment"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    "100",
                                                    FieldKey.MIN,
                                                    "1",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(new DegreeCalculation(null,
                                                      new Calculation(),
                                                      new Degree(),
                                                      Relevancy.HIGHLY,
                                                      BigDecimal.valueOf(200),
                                                      "Comment"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "DegreeCalculation",
                                                    FieldKey.FIELD,
                                                    "weight",
                                                    FieldKey.MAX,
                                                    "100",
                                                    FieldKey.MIN,
                                                    "1",
                                                    FieldKey.IS,
                                                    "200"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        DegreeCalculationValidationService spyService = spy(getService());

        Member member1 = createMember(1L, "Alice", "Smith");
        Member member2 = createMember(2L, "Bob", "Johnson");

        Degree degree = createDegree(1L, member1, "Degree 1");
        DegreeCalculation dc = new DegreeCalculation();
        dc.setCalculation(createCalculationWithMember(member2));
        dc.setDegree(degree);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(dc));

        assertEquals(ErrorKey.ATTRIBUTE_MATCHES, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should throw exception on duplicate degree ID")
    @Test
    void shouldThrowExceptionOnDuplicateDegreeId() {
        DegreeCalculationValidationService spyService = spy(getService());

        DegreeCalculation dc = getValidModel();
        List<DegreeCalculation> existing = List.of(dc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateDegreeId(dc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "degree", FieldKey.IS, "Degree 1"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        DegreeCalculationValidationService spyService = spy(getService());
        DegreeCalculation dc = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(any());

        spyService.validateOnCreate(dc);

        verify(spyService).validateMemberForCalculation(dc);
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member m = new Member();
        m.setId(id);
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setBirthDate(LocalDate.of(1980, 1, 1));
        m.setOrganisationUnit(new OrganisationUnit(1L, "Org Unit"));
        return m;
    }

    private Degree createDegree(Long id, Member member, String name) {
        Degree degree = new Degree();
        degree.setId(id);
        degree.setMember(member);
        degree.setName(name);
        return degree;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calc = new Calculation();
        calc.setId(id);
        calc.setMember(member);
        return calc;
    }

    private Calculation createCalculationWithMember(Member member) {
        Calculation calc = new Calculation();
        calc.setMember(member);
        return calc;
    }
}
