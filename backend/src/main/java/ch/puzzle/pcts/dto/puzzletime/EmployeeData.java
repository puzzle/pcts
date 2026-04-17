package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeData(Long id, String type, EmployeeAttributes attributes) {
}
