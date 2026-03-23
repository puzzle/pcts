package ch.puzzle.pcts.dto.puzzletime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Meta(@JsonProperty("current_page") int currentPage, @JsonProperty("total_pages") int totalPages) {
}
