package ch.puzzle.pctsmigration.experience;

import java.time.LocalDate;

public record ExperienceAiResultDto(String name, ExperienceType type, String employer, Integer percent, String comment,
        LocalDate startDate, LocalDate endDate) {
}
