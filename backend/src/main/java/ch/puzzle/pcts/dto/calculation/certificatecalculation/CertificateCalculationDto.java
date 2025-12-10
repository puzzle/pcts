package ch.puzzle.pcts.dto.calculation.certificatecalculation;

import ch.puzzle.pcts.dto.certificate.CertificateDto;

public record CertificateCalculationDto(Long id, CertificateDto certificate, String comment) {
}