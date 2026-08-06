package ch.puzzle.pctsmigration.model;

public interface ExtractionPipeline<C, T> {

    /** Unique identifier used for routing, e.g. "certificates", "degrees" */
    String name();

    /** Fetch all context needed from PCTS API */
    C fetchContext();

    /** Extract content from the .ods file and format it as a markdown table */
    String extractContent();

    /** Build the system prompt from the fetched context */
    String systemPrompt(C context, String content);

    /** Run validations on result objects */
    boolean validate();

    /** The output record class */
    Class<T> entityClass();
}