package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.exception.FileError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipleFileResultDto<D> {
    private final Map<String, List<D>> successfulCertificates = new HashMap<>();
    private final List<FileError> failedFiles = new ArrayList<>();

    public void addToSuccessfulCertificates(String fileName, List<D> successfulCertificates) {
        this.successfulCertificates.put(fileName, successfulCertificates);
    }

    public void addToFailedFiles(FileError failedFile) {
        this.failedFiles.add(failedFile);
    }
}