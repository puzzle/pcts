package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.exception.FileError;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.CertificateInputDto;

public class MultipleFileResultDto {
    private final List<CertificateInputDto> successfulCertificates = new ArrayList<>();
    private final List<FileError> failedFiles = new ArrayList<>();

    public List<CertificateInputDto> getSuccessfulCertificates() {
        return successfulCertificates;
    }

    public List<FileError> getFailedFiles() {
        return failedFiles;
    }
}