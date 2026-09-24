package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static org.apache.jena.rdf.model.impl.RDFDefaultErrorHandler.logger;

@Service
public class MatchingService {
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();
    public String match(List<String> options, String target) {
        double rangeInPercent = 50;
        int length = target.length();
        double rangeAdjustment = (length / 100.0) * rangeInPercent;

        double lowerEnd = length - rangeAdjustment;
        double upperEnd = length + rangeAdjustment;

        options = options.stream().filter(option -> option.length() >= lowerEnd && option.length() <= upperEnd).toList();

        options = options.stream().map(this::replaceUmlaute)
                .toList();

        String closest = options
                .stream()
                .min(Comparator.comparingInt(name -> calculateDistance(name, replaceUmlaute(target))))
                .orElseThrow();

        if (calculateDistance(closest, replaceUmlaute(target)) > 40) {
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
}


