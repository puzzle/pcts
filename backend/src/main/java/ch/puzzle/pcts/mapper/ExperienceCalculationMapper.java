package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExperienceCalculationMapper {
    private final ExperienceBusinessService experienceBusinessService;
    private final ExperienceMapper experienceMapper;

    public ExperienceCalculationMapper(ExperienceBusinessService experienceBusinessService,
                                       ExperienceMapper experienceMapper) {
        this.experienceBusinessService = experienceBusinessService;
        this.experienceMapper = experienceMapper;
    }

    public List<ExperienceCalculationDto> toDto(List<ExperienceCalculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<ExperienceCalculation> fromDto(List<ExperienceCalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public ExperienceCalculation fromDto(ExperienceCalculationInputDto dto) {
        return new ExperienceCalculation(dto
                .id(), null, experienceBusinessService.getById(dto.experienceId()), dto.relevancy(), dto.comment());
    }

    public ExperienceCalculationDto toDto(ExperienceCalculation model) {
        return new ExperienceCalculationDto(model.getId(),
                                            experienceMapper.toDto(model.getExperience()),
                                            model.getRelevancy(),
                                            model.getComment());
    }
}
