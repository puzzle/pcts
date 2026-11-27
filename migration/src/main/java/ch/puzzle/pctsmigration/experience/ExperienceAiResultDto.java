package ch.puzzle.pctsmigration.experience;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ExperienceAiResultDto(
        @NotNull
        @JsonPropertyDescription("""
                This is the name for an experience. it always appear in the 'Tätigkeit/Job' column. If nothing is specified, just set an empty string.
                """) String name,

        @NotNull
        @JsonPropertyDescription("""
                That's the kind of experience. There are 3 types: INTERNSHIP, WORK_EXPERIENCE, and SIDE_JOB.
                Select INTERNSHIP if you’re told in a different context that it’s an internship;
                        in that case, choose this type.
                Select 'WORK_EXPERIENCE' or 'SIDE_JOB' if you’re told this discreetly. In that case, make your decision accordingly:
                        select 'WORK_EXPERIENCE' if there’s something listed in the 'Activity/Job' column; if nothing is listed, select 'SIDE_JOB.'
                """) ExperienceType type,
        @NotNull
        @JsonPropertyDescription("""
                That's the employer from the relevant experience. You can always find the employer in the 'Arbeitgeber' column
                """) String employer,
        // TODO: Write Prompt
        @NotNull @JsonPropertyDescription("""
                """) Integer percent,
        @Nullable
        @JsonPropertyDescription("""
                This is a comment on an experience. The comment is always located in the field on the far right of the 'Bemerkung' column, if available.
                """) String comment,
        @NotNull @PastOrPresent @JsonPropertyDescription("""
                This is the start date of an Experience. The start date is located in the column 'von'
                """) LocalDate startDate, @NotNull @PastOrPresent @JsonPropertyDescription("""
                This is the end date of the experience. The end date is always listed in the 'bis' column.
                """) LocalDate endDate) {
}
