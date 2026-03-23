package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Ignoriert alle JSON-Felder, die wir nicht definiert haben (verhindert Abstürze)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PuzzleTimeResponseDto(List<EmployeeData> data, Meta meta) {
}
