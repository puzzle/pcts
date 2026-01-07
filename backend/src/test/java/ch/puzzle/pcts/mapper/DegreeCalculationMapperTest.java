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
    private static final Relevancy RELEVANCY = Relevancy.HIGHLY;
    private static final BigDecimal WEIGHT = BigDecimal.ONE;
    private static final String COMMENT = "Comment";

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

    private DegreeCalculation createDegreeCalculation(Degree degree) {
        return new DegreeCalculation(DEGREE_CALCULATION_ID, null, degree, RELEVANCY, WEIGHT, COMMENT);
    }

    private DegreeCalculationInputDto createDegreeCalculationInputDto() {
        return new DegreeCalculationInputDto(null, DEGREE_ID, WEIGHT, RELEVANCY, COMMENT);
    }

    private DegreeDto mockDegreeDto() {
        return mock(DegreeDto.class);
    }

    @DisplayName("Should map DegreeCalculation to DegreeCalculationDto")
    @Test
    void shouldMapToDto() {
        Degree degree = createDegree();
        DegreeCalculation model = createDegreeCalculation(degree);

        DegreeDto mockedDto = mockDegreeDto();
        when(degreeMapper.toDto(degree)).thenReturn(mockedDto);

        DegreeCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(DEGREE_CALCULATION_ID, result.id());
        assertEquals(mockedDto, result.degree());
        assertEquals(WEIGHT, result.weight());
        assertEquals(RELEVANCY, result.relevancy());
        assertEquals(COMMENT, result.comment());

        verify(degreeMapper).toDto(degree);
    }

    @DisplayName("Should map List<DegreeCalculation> to List<DegreeCalculationDto>")
    @Test
    void shouldMapListToDto() {
        Degree degree = createDegree();
        DegreeCalculation model = createDegreeCalculation(degree);

        when(degreeMapper.toDto(degree)).thenReturn(mockDegreeDto());

        List<DegreeCalculationDto> result = mapper.toDto(List.of(model));

        assertEquals(1, result.size());
        verify(degreeMapper).toDto(degree);
    }

    @DisplayName("Should map DegreeCalculationInputDto to DegreeCalculation")
    @Test
    void shouldMapFromDto() {
        Degree degree = createDegree();
        DegreeCalculationInputDto input = createDegreeCalculationInputDto();

        when(degreeBusinessService.getById(DEGREE_ID)).thenReturn(degree);

        DegreeCalculation result = mapper.fromDto(input);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(degree, result.getDegree());
        assertEquals(RELEVANCY, result.getRelevancy());
        assertEquals(WEIGHT, result.getWeight());
        assertEquals(COMMENT, result.getComment());

        verify(degreeBusinessService).getById(DEGREE_ID);
    }

    @DisplayName("Should map List<DegreeCalculationInputDto> to List<DegreeCalculation>")
    @Test
    void shouldMapListFromDto() {
        Degree degree = createDegree();
        DegreeCalculationInputDto input = createDegreeCalculationInputDto();

        when(degreeBusinessService.getById(DEGREE_ID)).thenReturn(degree);

        List<DegreeCalculation> result = mapper.fromDto(List.of(input));

        assertEquals(1, result.size());
        verify(degreeBusinessService).getById(DEGREE_ID);
    }
}
