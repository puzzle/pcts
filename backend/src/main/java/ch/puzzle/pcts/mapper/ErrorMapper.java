package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.model.error.GenericError;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ErrorMapper {
    public List<GenericErrorDto> toDto(List<GenericError> models) {
        return models.stream().map(this::toDto).toList();
    }

    public GenericErrorDto toDto(GenericError model) {
        return new GenericErrorDto(model.getKey(), model.getValues());
    }
}
