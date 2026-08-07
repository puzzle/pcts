package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.certificate.CertificateAiResultDto;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Function;

public interface ExtractionPipeline<C, R, D> {

    /** Unique identifier used for routing, e.g. "certificates", "degrees" */
    String name();

    /** Fetch all context needed from PCTS API */
    C fetchContext() throws ApiException;

    /** Build the system prompt from the fetched context */
    String systemPrompt(C context);

    /** Run validations on result objects */
    boolean validate();

    /** The result record class */
    Class<R> entityClass();

    /**
     * Map result record to effective DTO
     */
    Function<R, List<D>> mapToDto();
}