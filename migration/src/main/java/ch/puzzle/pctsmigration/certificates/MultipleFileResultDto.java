package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.exception.FileError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openapitools.client.model.CertificateInputDto;

public class MultipleFileResultDto {
    private final Map<String, List<CertificateInputDto>> successfulCertificates = new HashMap<>();
    private final List<FileError> failedFiles = new ArrayList<>();

    public void addToSuccessfulCertificates(String fileName, List<CertificateInputDto> successfulCertificates) {
        this.successfulCertificates.put(fileName, successfulCertificates);
    }

    public void addToFailedFiles(FileError failedFile) {
        this.failedFiles.add(failedFile);
    }

    public Map<String, List<CertificateInputDto>> getSuccessfulCertificates() {
        return successfulCertificates;
    }

    public List<FileError> getFailedFiles() {
        return failedFiles;
    }
}