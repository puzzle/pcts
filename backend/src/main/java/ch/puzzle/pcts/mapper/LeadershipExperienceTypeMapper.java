package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceTypeMapper {

    public List<LeadershipExperienceTypeDto> toDto(List<LeadershipExperienceType> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<LeadershipExperienceType> fromDto(List<LeadershipExperienceTypeDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceTypeDto toDto(LeadershipExperienceType model) {
        return new LeadershipExperienceTypeDto(model
                .getId(), model.getName(), model.getPoints(), model.getComment(), model.getExperienceKind());

    }

    public LeadershipExperienceType fromDto(LeadershipExperienceTypeDto dto) {
        return LeadershipExperienceType.Builder
                .builder()
                .withId(dto.id())
                .withName(dto.name())
                .withPoints(dto.points())
                .withComment(dto.comment())
                .withExperienceKind(dto.leadershipExperienceKind())
                .build();
    }
}
