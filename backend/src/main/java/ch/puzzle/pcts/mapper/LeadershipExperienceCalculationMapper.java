package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceCalculationMapper {

    private final LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    private final LeadershipExperienceMapper leadershipExperienceMapper;

    public LeadershipExperienceCalculationMapper(LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                                 LeadershipExperienceMapper leadershipExperienceMapper) {
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.leadershipExperienceMapper = leadershipExperienceMapper;

    }

    public List<LeadershipExperienceCalculationDto> toDto(List<LeadershipExperienceCalculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<LeadershipExperienceCalculation> fromDto(List<LeadershipExperienceCalculationInputDto> ids) {
        return ids.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceCalculation fromDto(LeadershipExperienceCalculationInputDto dto) {
        return new LeadershipExperienceCalculation(dto
                .id(), null, leadershipExperienceBusinessService.getById(dto.leadershipExperienceId()));
    }

    public LeadershipExperienceCalculationDto toDto(LeadershipExperienceCalculation model) {
        return new LeadershipExperienceCalculationDto(model
                .getId(), leadershipExperienceMapper.toDto(model.getLeadershipExperience()));
    }
}
