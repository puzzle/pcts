package ch.puzzle.pcts.dto.calculation.certificatecalculation;

import io.swagger.v3.oas.annotations.media.Schema;

public record CertificateCalculationInputDto(
        @Schema(description = "Unique identifier of the certificate calculation", nullable = true, example = "null") Long id,

        @Schema(description = "Id of the certificate associated with the calculation", example = "1") Long certificateId) {
}