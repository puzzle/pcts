package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
    private static final double RANGE_IN_PERCENT = 40;
    private static final double PERCENT_FACTOR = RANGE_IN_PERCENT / 100;

    public String match(List<String> options, String target) {
        String normalizedTarget = replaceUmlaute(target);
        List<String> filteredOptions = filterByLengthThreshold(options, normalizedTarget);

        if (filteredOptions.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "No valid option found within acceptable range"));
        }

        List<String> sorted = filteredOptions
                .stream()
                .sorted(Comparator.comparingInt(name -> calculateDistance(replaceUmlaute(name), normalizedTarget)))
                .toList();

        String first = sorted.getFirst();

        if (calculateDistance(first, normalizedTarget) > 40) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Levenshtein distance is too large to be a valid insert"));
        }

        if (filteredOptions.size() == 1) {
            return first;
        }

        String second = sorted.get(1);

        if (Objects.equals(calculateDistance(first, target), calculateDistance(second, target))) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Two options are equally close to target"));
        }

        return first;
    }

    private Integer calculateDistance(String dtoName, String name) {
        Integer distance = this.levenshtein.apply(dtoName.toLowerCase(), name.toLowerCase());
        logger.info("Input name: {}, Actual name: {}, Distance: {}", name, dtoName, distance);

        return distance;
    }

    private String replaceUmlaute(String input) {
        return input
                .replace("ä", "ae")
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

        return list.stream().filter(option -> option.length() >= lower && option.length() <= upper).toList();
    }
}
