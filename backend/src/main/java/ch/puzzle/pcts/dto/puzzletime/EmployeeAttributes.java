package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmployeeAttributes(String firstname, String lastname, @JsonProperty("ldapname") String ldapName,
        String shortname, LocalDate birthday, @JsonProperty("is_employed") boolean isEmployed,
        @JsonProperty("department_name") String departmentName) {
}
