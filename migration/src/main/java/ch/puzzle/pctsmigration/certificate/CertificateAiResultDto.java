package ch.puzzle.pctsmigration.certificate;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.time.LocalDate;

public record CertificateAiResultDto(
        @JsonPropertyDescription("This date indicates when the certificate was completed. It is always listed in the “Datum” column.")
        LocalDate completedAt,

        @JsonPropertyDescription("This is a comment on the certificate. This is always the farthest to the right field in a certificate line. If it is empty, set the comment to “null.”")
        String comment,

        @JsonPropertyDescription("""
                You are a data-mapping assistant.
                Your task is to find the best match from a list of all types. The name and the score can help you based on the input.
                Each of these types has specific characteristics and a unique ID.
                Carefully analyze the name and points in the input to determine which type best fits.
                Once you have determined the most appropriate match, take the unique ID of that type and enter it into the designated attribute.
                Please return only this identified ID as the result and do not include any additional text or explanations.
                """)
        Long certificateTypeId
) {
}
