package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceCalculationMapperTest {

    private static final Long EXPERIENCE_ID = 11L;
    private static final Long EXPERIENCE_CALCULATION_ID = 22L;

    private static final Relevancy RELEVANCY = Relevancy.HIGHLY;

    @Mock
    private ExperienceBusinessService experienceBusinessService;

    @Mock
    private ExperienceMapper experienceMapper;

    private ExperienceCalculationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ExperienceCalculationMapper(experienceBusinessService, experienceMapper);
    }

    private Experience createExperience() {
        Experience exp = new Experience();
        exp.setId(EXPERIENCE_ID);
        return exp;
    }

    private ExperienceCalculation createModel(Experience experience) {
        return new ExperienceCalculation(EXPERIENCE_CALCULATION_ID, null, experience, RELEVANCY, "Comment");
    }

    @DisplayName("Should map ExperienceCalculation to ExperienceCalculationDto")
    @Test
    void shouldMapToDto() {
        Experience experience = createExperience();
        ExperienceCalculation model = createModel(experience);

        ExperienceDto expectedExperienceDto = mock(ExperienceDto.class);
        when(experienceMapper.toDto(experience)).thenReturn(expectedExperienceDto);

        ExperienceCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(EXPERIENCE_CALCULATION_ID, result.id());
        assertEquals(expectedExperienceDto, result.experience());
        assertEquals(RELEVANCY, result.relevancy());
        assertEquals("Comment", result.comment());

        verify(experienceMapper).toDto(experience);
    }

    @DisplayName("Should map List<ExperienceCalculation> to List<ExperienceCalculationDto>")
    @Test
    void shouldMapListToDto() {
        Experience experience = createExperience();
        ExperienceCalculation model = createModel(experience);

        when(experienceMapper.toDto(experience)).thenReturn(mock(ExperienceDto.class));

        List<ExperienceCalculationDto> result = mapper.toDto(List.of(model));

        assertEquals(1, result.size());
        verify(experienceMapper).toDto(experience);
    }

    @DisplayName("Should map ExperienceCalculationInputDto to ExperienceCalculation")
    @Test
    void shouldMapFromDto() {
        Experience experience = createExperience();
        ExperienceCalculationInputDto input = new ExperienceCalculationInputDto(EXPERIENCE_ID, RELEVANCY, "Comment");

        when(experienceBusinessService.getById(EXPERIENCE_ID)).thenReturn(experience);

        ExperienceCalculation result = mapper.fromDto(input);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(experience, result.getExperience());
        assertEquals(RELEVANCY, result.getRelevancy());
        assertEquals("Comment", result.getComment());

        verify(experienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should map List<ExperienceCalculationInputDto> to List<ExperienceCalculation>")
    @Test
    void shouldMapListFromDto() {
        Experience experience = createExperience();
        ExperienceCalculationInputDto input = new ExperienceCalculationInputDto(EXPERIENCE_ID, RELEVANCY, "Comment");

        when(experienceBusinessService.getById(EXPERIENCE_ID)).thenReturn(experience);

        List<ExperienceCalculation> result = mapper.fromDto(List.of(input));

        assertEquals(1, result.size());
        verify(experienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should throw when Experience not found")
    @Test
    void shouldThrowWhenExperienceNotFound() {
        ExperienceCalculationInputDto input = new ExperienceCalculationInputDto(EXPERIENCE_ID, RELEVANCY, "Comment");

        when(experienceBusinessService.getById(EXPERIENCE_ID)).thenThrow(new RuntimeException("Experience not found"));

        assertThrows(RuntimeException.class, () -> mapper.fromDto(input));
    }
}
