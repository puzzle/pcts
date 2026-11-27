package ch.puzzle.pctsmigration.certificate;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public record CertificateWrapper(
        List<CertificateAiResultDto> items) implements AiResultWrapper<CertificateAiResultDto> {
}
