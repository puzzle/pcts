package ch.puzzle.pcts.dto.calculation.certificatecalculation;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record CertificateCalculationDto(

        @Schema(description = "Unique identifier of the certificate calculation", example = "15") Long id,

        @Schema(description = "Certificate associated with the calculation") CertificateDto certificate) {
}