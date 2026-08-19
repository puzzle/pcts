package ch.puzzle.pctsmigration.certificates;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CertificateWrapper(@Valid @NotNull List<CertificateAiResultDto> items) {
}
