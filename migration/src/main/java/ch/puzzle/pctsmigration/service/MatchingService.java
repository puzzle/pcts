package ch.puzzle.pctsmigration.service;

import org.apache.commons.text.similarity.LevenshteinDistance;
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

        return options
                .stream()
                .min(Comparator.comparingInt(dto -> calculateDistance(dto.toLowerCase(), replaceUmlaute(target.toLowerCase()))))
                .orElseThrow();
    }

    private Integer calculateDistance(String dtoName, String name) {
        Integer distance = this.levenshtein.apply(dtoName, name);
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


