package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Ignores all JSON fields that we did not define, which prevents crashes
@JsonIgnoreProperties(ignoreUnknown = true)
public record PuzzleTimeResponseDto(List<EmployeeData> data, Meta meta) {
}
