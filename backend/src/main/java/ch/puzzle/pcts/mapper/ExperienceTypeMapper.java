package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExperienceTypeMapper {

    public List<ExperienceTypeDto> toDto(List<ExperienceType> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<ExperienceType> fromDto(List<ExperienceTypeDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public ExperienceTypeDto toDto(ExperienceType model) {
        return new ExperienceTypeDto(model.getId(),
                                     model.getName(),
                                     model.getHighlyRelevantPoints(),
                                     model.getLimitedRelevantPoints(),
                                     model.getLittleRelevantPoints());
    }

    public ExperienceType fromDto(ExperienceTypeDto dto) {
        return ExperienceType.Builder.builder()
                .withId(dto.id())
                .withName(dto.name())
                .withHighlyRelevantPoints(dto.highlyRelevantPoints())
                .withLimitedRelevantPoints(dto.limitedRelevantPoints())
                .withLittleRelevantPoints(dto.littleRelevantPoints())
                .build();
    }
}
