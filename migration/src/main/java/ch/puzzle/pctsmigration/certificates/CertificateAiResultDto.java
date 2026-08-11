package ch.puzzle.pctsmigration.certificates;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CertificateAiResultDto(@NotNull @NotBlank
@JsonPropertyDescription("This attribute is the name of a certificate. It always appears in the 'Zertifikat' column.") String name,

        @PastOrPresent
        @JsonPropertyDescription("This date indicates when the certificate was completed. It is always listed in the “Datum” column.") LocalDate completedAt,

        @Nullable
        @JsonPropertyDescription("This is a comment on the certificate. This is always the farthest to the right field in a certificate line. If it is empty, set the comment to “null.”") String comment,

        @NotNull
        @JsonPropertyDescription("When processing the provided dataset to determine the point value of a certificate, please interpret the scoring columns as a binary indicator system. \n"
                                 + "          The data contains five dedicated columns whose headers explicitly represent the possible point categories: \"0\", \"0.5\", \"1\", \"1.5\", and \"2\". For every individual row, \n"
                                 + "          the actual point value awarded is exclusively indicated by the presence of the integer '1' inside exactly one of these five columns. \n"
                                 + "          To extract the correct score for a given row, you must scan these specific columns, locate the single cell containing the '1', \n"
                                 + "          and assign the numerical value of that column's header as the final score for that certificate. For example, \n"
                                 + "          if a row features a '1' within the column labeled \"0.5\", you must evaluate that specific certificate as being worth exactly 0.5 points, recognizing that the other point-category columns for that same row will not contain the active '1' indicator.") BigDecimal points) {
}
