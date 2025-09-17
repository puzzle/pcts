package ch.puzzle.pcts.dto.certificate;

import java.math.BigDecimal;

public record CertificateDto(Long id, String name, BigDecimal points, String comment) {
}
