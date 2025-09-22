package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeDto;
import ch.puzzle.pcts.dto.degreeType.DegreeTypeWithoutIdDto;
import ch.puzzle.pcts.model.degreeType.DegreeType;
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

    public DegreeType fromDto(DegreeTypeWithoutIdDto dto) {
        return new DegreeType(null,
                              dto.name(),
                              dto.highly_relevant_points(),
                              dto.limited_relevant_points(),
                              dto.little_relevant_points());
    }

    public DegreeTypeDto toDto(DegreeType model) {
        return new DegreeTypeDto(model.getDegreeTypeId(),
                                 model.getName(),
                                 model.getHighlyRelevantPoints(),
                                 model.getLimitedRelevantPoints(),
                                 model.getLittleRelevantPoints());
    }

    public DegreeType fromDto(DegreeTypeDto dto) {
        return new DegreeType(dto.degreeTypeId(),
                              dto.name(),
                              dto.highlyRelevantPoints(),
                              dto.limitedRelevantPoints(),
                              dto.littleRelevantPoints());
    }
}
