package ch.puzzle.pcts.service.validation.util;

import ch.puzzle.pcts.model.Model;
import java.util.Optional;
import java.util.function.Function;

public class UniqueNameValidationUtil {

    private UniqueNameValidationUtil() {
    }

    public static <T extends Model> boolean nameAlreadyUsed(String valueToFind, Function<String, Optional<T>> finder) {
        return finder.apply(valueToFind).isPresent();
    }

    public static <T extends Model> boolean nameExcludingSelfAlreadyUsed(Long id, String valueToFind,
                                                                         Function<String, Optional<T>> finder) {
        return finder.apply(valueToFind).map(entity -> !entity.getId().equals(id)).orElse(false);
    }
}
