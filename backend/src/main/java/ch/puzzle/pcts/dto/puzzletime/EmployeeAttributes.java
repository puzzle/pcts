package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeAttributes(String firstname, String lastname, String shortname,
        @JsonProperty("is_employed") boolean isEmployed, @JsonProperty("department_name") String departmentName) {
}
