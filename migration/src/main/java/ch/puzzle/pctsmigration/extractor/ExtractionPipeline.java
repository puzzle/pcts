package ch.puzzle.pctsmigration.extractor;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openapitools.client.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;

public abstract class ExtractionPipeline<C, R, D> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();

    public String extractAbbreviation(String filename) {
        if (filename.contains("_")) {
            return filename.split("_")[0].toUpperCase();
        }
        throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                               "Invalid filename: can not extract abbreviation " + filename));
    }

    public Integer calculateDistance(String dtoName, String name) {
        Integer distance = this.levenshtein.apply(dtoName, name);
        logger.info("Input name: {}, Actual name: {}, Distance: {}", name, dtoName, distance);

        return distance;
    }

    /** Fetch all context needed from the PCTS API or other places */
    public abstract C fetchContext();

    /** Build the system prompt from the fetched context */
    public abstract String systemPrompt(C context);

    /** defines which sheets will be extracted in OdsParserService */
    // TODO: lamda-expression Function interface
    public abstract OdsParseConfig odsSheetParseConfig();

    /**
     * Run validations on result objects throw MigrationException when something is
     * wrong
     */
    public void additionalValidations(R toValidate) {
    }

    /** The result record class */
    public abstract Class<R> entityClass();

    /** Map result record to effective DTO */
    public abstract List<D> mapToDto(String filename, R wrapper);

    /** Send the results to the PCTS API to create a new resource */
    public abstract void create(List<D> dtos) throws ApiException;
}