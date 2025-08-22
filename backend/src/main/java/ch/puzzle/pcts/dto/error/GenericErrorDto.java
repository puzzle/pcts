package ch.puzzle.pcts.dto.error;

import ch.puzzle.pcts.model.error.ErrorKey;
import java.util.List;

public record GenericErrorDto(ErrorKey key, List<String> reasons) {
}
