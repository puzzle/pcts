package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.exception.FileError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object that holds the results of a multiple file endpoint
 *
 * @param <D>
 *            The type of the InputDto representing the specific models
 *            extracted from the files.
 */
public class MultipleFileResultDto<D> {
    private final Map<String, List<D>> successfulFiles = new HashMap<>();
    private final List<FileError> failedFiles = new ArrayList<>();

    public void addToSuccessfulFiles(String fileName, List<D> successfulFiles) {
        this.successfulFiles.put(fileName, successfulFiles);
    }

    public void addToFailedFiles(FileError failedFile) {
        this.failedFiles.add(failedFile);
    }

    public Map<String, List<D>> getSuccessfulFiles() {
        return successfulFiles;
    }

    public List<FileError> getFailedFiles() {
        return failedFiles;
    }
}