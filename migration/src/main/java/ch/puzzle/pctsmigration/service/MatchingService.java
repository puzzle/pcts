package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MatchingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
    private static final double RANGE_IN_PERCENT = 40;
    private static final double PERCENT_FACTOR = RANGE_IN_PERCENT / 100;

    public String match(List<String> options, String target) {
        String normalizedTarget = replaceUmlaute(target);
        List<String> filteredOptions = filterByLengthThreshold(options, normalizedTarget);

        String closest = filteredOptions
                .stream()
                .min(Comparator.comparingInt(name -> calculateDistance(replaceUmlaute(name), normalizedTarget)))
                .orElseThrow();

        if (calculateDistance(closest, normalizedTarget) > 40) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "Levenshtein distance is too large to be a valid insert"));
        }

        return closest;
    }

    private Integer calculateDistance(String dtoName, String name) {
        Integer distance = this.levenshtein.apply(dtoName.toLowerCase(), name.toLowerCase());
        logger.info("Input name: {}, Actual name: {}, Distance: {}", name, dtoName, distance);

        return distance;
    }

    private String replaceUmlaute(String input) {
        return input.replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue");
    }

    private List<String> filterByLengthThreshold(List<String> list, String normalizedTarget) {
        double adjustment = normalizedTarget.length() * PERCENT_FACTOR;

        double lower = normalizedTarget.length() - adjustment;
        double upper = lower + adjustment * 2;

        List<String> filteredOptions = list.stream().filter(option -> option.length() >= lower && option.length() <= upper).toList();

        if (filteredOptions.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "No valid option found within acceptable range"));
        }

        return filteredOptions;
    }
}


