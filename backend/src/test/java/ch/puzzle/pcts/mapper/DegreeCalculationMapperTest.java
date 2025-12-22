package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeCalculationMapperTest {

    private static final Long DEGREE_ID = 100L;

    private static final Long DEGREE_CALCULATION_ID = 20L;

    @Mock
    private DegreeBusinessService degreeBusinessService;

    @Mock
    private DegreeMapper degreeMapper;

    private DegreeCalculationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DegreeCalculationMapper(degreeBusinessService, degreeMapper);
    }

    private Degree createDegree() {
        Degree degree = new Degree();
        degree.setId(DEGREE_ID);
        return degree;
    }

    private DegreeCalculation createModel(Degree degree) {
        return new DegreeCalculation(DEGREE_CALCULATION_ID, null, degree, Relevancy.HIGHLY, BigDecimal.ONE, "Comment");
    }

    @DisplayName("Should map DegreeCalculation to DegreeCalculationDto")
    @Test
    void shouldMapToDto() {
        Degree degree = createDegree();
        DegreeCalculation model = createModel(degree);

        DegreeDto mockedDegreeDto = mock(DegreeDto.class);

        when(degreeMapper.toDto(degree)).thenReturn(mockedDegreeDto);

        DegreeCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(DEGREE_CALCULATION_ID, result.id());
        assertEquals(mockedDegreeDto, result.degree());
        assertEquals(BigDecimal.ONE, result.weight());
        assertEquals(Relevancy.HIGHLY, result.relevancy());
        assertEquals("Comment", result.comment());

        verify(degreeMapper).toDto(degree);
    }

    @DisplayName("Should map List<DegreeCalculation> to List<DegreeCalculationDto>")
    @Test
    void shouldMapListToDto() {
        Degree degree = createDegree();
        DegreeCalculation model = createModel(degree);

        when(degreeMapper.toDto(degree)).thenReturn(mock(DegreeDto.class));

        List<DegreeCalculationDto> result = mapper.toDto(List.of(model));

        assertEquals(1, result.size());
        verify(degreeMapper).toDto(degree);
    }

    @DisplayName("Should map DegreeCalculationInputDto to DegreeCalculation")
    @Test
    void shouldMapFromDto() {
        Degree degree = createDegree();
        DegreeCalculationInputDto input = new DegreeCalculationInputDto(DEGREE_ID,
                                                                        BigDecimal.ONE,
                                                                        Relevancy.HIGHLY,
                                                                        "Comment");

        when(degreeBusinessService.getById(DEGREE_ID)).thenReturn(degree);

        DegreeCalculation result = mapper.fromDto(input);

        assertNotNull(result);
        assertEquals(degree, result.getDegree());
        assertEquals(Relevancy.HIGHLY, result.getRelevancy());
        assertEquals(BigDecimal.ONE, result.getWeight());
        assertNull(result.getId());
        assertEquals("Comment", result.getComment());

        verify(degreeBusinessService).getById(DEGREE_ID);
    }

    @DisplayName("Should map List<DegreeCalculationInputDto> to List<DegreeCalculation>")
    @Test
    void shouldMapListFromDto() {
        Degree degree = createDegree();
        DegreeCalculationInputDto input = new DegreeCalculationInputDto(DEGREE_ID,
                                                                        BigDecimal.ONE,
                                                                        Relevancy.HIGHLY,
                                                                        "Comment");

        when(degreeBusinessService.getById(DEGREE_ID)).thenReturn(degree);

        List<DegreeCalculation> result = mapper.fromDto(List.of(input));

        assertEquals(1, result.size());
        verify(degreeBusinessService).getById(DEGREE_ID);
    }

    @DisplayName("Should throw when Degree not found")
    @Test
    void shouldThrowWhenDegreeNotFound() {
        DegreeCalculationInputDto input = new DegreeCalculationInputDto(DEGREE_ID,
                                                                        BigDecimal.ONE,
                                                                        Relevancy.HIGHLY,
                                                                        "Comment");

        when(degreeBusinessService.getById(DEGREE_ID)).thenThrow(new RuntimeException("Degree not found"));

        assertThrows(RuntimeException.class, () -> mapper.fromDto(input));
    }
}
