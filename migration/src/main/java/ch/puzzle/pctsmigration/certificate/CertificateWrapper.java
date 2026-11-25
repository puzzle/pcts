package ch.puzzle.pctsmigration.certificate;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public class CertificateWrapper extends AiResultWrapper<CertificateAiResultDto> {

    public CertificateWrapper() {
        super(null);
    }

    public CertificateWrapper(List<CertificateAiResultDto> items) {
        super(items);
    }
}
