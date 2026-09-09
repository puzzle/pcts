package ch.puzzle.pctsmigration.extractor;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;

public abstract class Pipeline {
    private final static Logger logger = LoggerFactory.getLogger(Pipeline.class);
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
}
