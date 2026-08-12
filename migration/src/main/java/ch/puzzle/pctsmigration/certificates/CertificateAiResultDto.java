package ch.puzzle.pctsmigration.certificates;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CertificateAiResultDto(@NotNull @NotBlank
@JsonPropertyDescription("This attribute is the name of a certificate. It always appears in the 'Zertifikat' column.") String name,

        @PastOrPresent
        @JsonPropertyDescription("This date indicates when the certificate was completed. It is always listed in the “Datum” column."
                                 + "It's also possible that you won't always get a properly formatted date. "
                                 + "If only the month and year are specified, use the first day of the month.") LocalDate completedAt,

        @Nullable
        @JsonPropertyDescription("This is a comment on the certificate. This is always the farthest to the right field in a certificate line. "
                                 + "If it is empty, set the comment to “null.”") String comment) {
}
