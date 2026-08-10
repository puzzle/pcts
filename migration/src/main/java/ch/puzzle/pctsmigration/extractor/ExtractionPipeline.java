package ch.puzzle.pctsmigration.extractor;

import org.openapitools.client.ApiException;

import java.util.List;

public interface ExtractionPipeline<C, R, D> {

    /** Fetch all context needed from PCTS API */
    C fetchContext() throws ApiException;

    /** Build the system prompt from the fetched context */
    String systemPrompt(C context);

    /** Run validations on result objects */
    boolean validate();

    /** The result record class */
    Class<R> entityClass();

    /** Map result record to effective DTO */
    List<D> mapToDto(String filename, R wrapper);

    /** send result to the pcts-api to create */
    void create(List<D> dtos) throws ApiException;
}