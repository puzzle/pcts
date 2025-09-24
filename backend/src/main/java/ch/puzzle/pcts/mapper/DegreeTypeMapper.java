package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.degree_type.DegreeTypeDto;
import ch.puzzle.pcts.model.degree_type.DegreeType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DegreeTypeMapper {
    public List<DegreeTypeDto> toDto(List<DegreeType> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<DegreeType> fromDto(List<DegreeTypeDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public DegreeTypeDto toDto(DegreeType model) {
        return new DegreeTypeDto(model.getId(),
                                 model.getName(),
                                 model.getHighlyRelevantPoints(),
                                 model.getLimitedRelevantPoints(),
                                 model.getLittleRelevantPoints());
    }

    public DegreeType fromDto(DegreeTypeDto dto) {
        return new DegreeType(dto
                .id(), dto.name(), dto.highlyRelevantPoints(), dto.limitedRelevantPoints(), dto.littleRelevantPoints());
    }
}
