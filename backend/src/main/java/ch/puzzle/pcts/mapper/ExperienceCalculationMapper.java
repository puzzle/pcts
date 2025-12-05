package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExperienceCalculationMapper {
    private final ExperienceBusinessService experienceBusinessService;

    public ExperienceCalculationMapper(ExperienceBusinessService experienceBusinessService) {
        this.experienceBusinessService = experienceBusinessService;
    }

    public List<ExperienceCalculation> fromDto(List<ExperienceCalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public ExperienceCalculation fromDto(ExperienceCalculationInputDto dto) {
        return new ExperienceCalculation(null,
                                         null,
                                         experienceBusinessService.getById(dto.experienceId()),
                                         dto.relevancy());
    }
}
