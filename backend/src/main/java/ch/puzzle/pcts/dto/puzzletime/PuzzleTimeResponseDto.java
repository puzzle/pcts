package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Ignoriert alle JSON-Felder, die wir nicht definiert haben (verhindert Abstürze)
@JsonIgnoreProperties(ignoreUnknown = true)
public record PuzzleTimeResponseDto(List<EmployeeData> data, Meta meta) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeData(Long id, String type, EmployeeAttributes attributes) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeAttributes(String firstname, String lastname, String shortname,
        @JsonProperty("is_employed") boolean isEmployed, @JsonProperty("department_name") String departmentName) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
public record Meta(@JsonProperty("current_page") int currentPage, @JsonProperty("total_pages") int totalPages) {
}
