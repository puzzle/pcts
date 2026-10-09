package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class MatchingService<T> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
    private static final double PERCENT_FACTOR = 0.40;

    public T match(List<T> options, String target, Function<T, String> getTargetAttr) {
        String normalizedTarget = replaceUmlaute(target);
        List<T> filteredOptions = filterByLengthThreshold(options, target, getTargetAttr);

        if (filteredOptions.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "No valid option found within acceptable range"));
        }

        List<Match<T>> sortedOptions = filteredOptions.stream().map(option -> {
            String attr = getTargetAttr.apply(option);
            String normalizedAttr = replaceUmlaute(attr);
            int distance = calculateDistance(normalizedAttr, normalizedTarget);
            return new Match<>(option, distance, attr);
        }).sorted(Comparator.comparingInt(Match::distance)).toList();

        Match<T> firstMatch = sortedOptions.getFirst();

        if (calculateDistance(firstMatch.originalAttr(), normalizedTarget) > 40) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Levenshtein distance is too large to be a valid insert"));
        }

        if (sortedOptions.size() == 1) {
            return firstMatch.option();
        }

        Match<T> secondMatch = sortedOptions.get(1);

        int distanceFirst = calculateDistance(firstMatch.originalAttr(), target);
        int distanceSecond = calculateDistance(secondMatch.originalAttr(), target);

        if (distanceFirst == distanceSecond) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Two options are equally close to target"));
        }

        return firstMatch.option();
    }

    private int calculateDistance(String dtoName, String name) {
        int distance = this.levenshtein.apply(dtoName.toLowerCase(), name.toLowerCase());
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