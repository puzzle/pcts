package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MatchingService<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
    private static final double PERCENT_FACTOR = 0.40;
    private static final int MAX_DISTANCE = 40;

    public T match(List<T> validResults, String target, Function<T, String> getTargetAttr) {
        List<T> filteredValidResults = filterByLengthThreshold(validResults, target, getTargetAttr);

        if (filteredValidResults.isEmpty()) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST,
                                                   "No valid option found within acceptable range"));
        }

        List<Match<T>> sortedValidResults = filteredValidResults.stream().map(option -> {
            String attr = getTargetAttr.apply(option);
            int distance = calculateDistance(attr, target);
            return new Match<>(option, distance, attr);
        }).sorted(Comparator.comparingInt(Match::distance)).toList();

        Match<T> firstMatch = sortedValidResults.getFirst();

        if (calculateDistance(firstMatch.originalAttr(), target) > MAX_DISTANCE) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST,
                                                   "Levenshtein distance is too large to be a valid insert"));
        }

        if (sortedValidResults.size() == 1) {
            return firstMatch.option();
        }

        Match<T> secondMatch = sortedValidResults.get(1);

        if (firstMatch.distance == secondMatch.distance) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST, "Two options are equally close to target"));
        }

        return firstMatch.option();
    }

    private int calculateDistance(String dtoName, String name) {
        int distance = this.levenshtein
                .apply(replaceUmlaute(dtoName.toLowerCase()), replaceUmlaute(name.toLowerCase()));
        logger.info("Input name: {}, Actual name: {}, Distance: {}", name, dtoName, distance);
        return distance;
    }

    private String replaceUmlaute(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue");
    }

    // To improve performance, we filter out any option that is outside a certain
    // percent range in length.
    private List<T> filterByLengthThreshold(List<T> list, String target, Function<T, String> getTargetAttr) {
        int targetLength = target.length();
        double adjustment = targetLength * PERCENT_FACTOR;
        double lower = targetLength - adjustment;
        double upper = targetLength + adjustment;

        return list.stream().filter(option -> {
            int optionLength = getTargetAttr.apply(option).length();
            return optionLength >= lower && optionLength <= upper;
        }).toList();
    }

    private record Match<T>(T option, int distance, String originalAttr) {
    }
}