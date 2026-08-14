package ch.puzzle.pctsmigration.extractor;

import java.util.List;
import org.openapitools.client.ApiException;

public interface ExtractionPipeline<C, R, D> {

    /** Fetch all context needed from the PCTS API or other places */
    C fetchContext();

    /** Build the system prompt from the fetched context */
    String systemPrompt(C context);

    /**
     * Run validations on result objects throw MigrationException when something is
     * wrong
     */
    void additionalValidations(R toValidate);

    /** The result record class */
    Class<R> entityClass();

    /** Map result record to effective DTO */
    List<D> mapToDto(String filename, R wrapper);

    /** Send the results to the PCTS API to create a new resource */
    void create(List<D> dtos) throws ApiException;
}