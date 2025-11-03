package ch.puzzle.pcts.service.validation.util;

import ch.puzzle.pcts.model.Model;
import java.util.Optional;
import java.util.function.Function;

/**
 * Utility class providing helper methods for validating the uniqueness of
 * entity names within the PCTS domain model.
 * <p>
 * This class offers reusable static methods to check whether an entity already
 * uses a given name, optionally excluding a specific entity by ID.
 * </p>
 */
public final class UniqueNameValidationUtil {

    private UniqueNameValidationUtil() {
    }

    /**
     * Checks whether a given name is already used by any existing entity.
     *
     * @param <T>
     *            the type of the entity, which must extend {@link Model}
     * @param valueToFind
     *            the name value to check for uniqueness
     * @param finder
     *            a function that takes a name and returns an {@link Optional}
     *            containing the matching entity if found, or empty otherwise
     * @return {@code true} if an entity with the specified name already exists;
     *         {@code false} otherwise
     */
    public static <T extends Model> boolean nameAlreadyUsed(String valueToFind, Function<String, Optional<T>> finder) {
        return finder.apply(valueToFind).isPresent();
    }

    /**
     * Checks whether a given name is already used by another entity, excluding the
     * entity with the specified ID.
     * <p>
     * This is useful for update operations, where an entity should be allowed to
     * retain its current name without triggering a uniqueness violation.
     * </p>
     *
     * @param <T>
     *            the type of the entity, which must extend {@link Model}
     * @param id
     *            the ID of the entity to exclude from the uniqueness check
     * @param valueToFind
     *            the name value to check for uniqueness
     * @param finder
     *            a function that takes a name and returns an {@link Optional}
     *            containing the matching entity if found, or empty otherwise
     * @return {@code true} if another entity (with a different ID) already uses the
     *         specified name; {@code false} otherwise
     */
    public static <T extends Model> boolean nameExcludingSelfAlreadyUsed(Long id, String valueToFind,
                                                                         Function<String, Optional<T>> finder) {
        return finder.apply(valueToFind).map(entity -> !entity.getId().equals(id)).orElse(false);
    }
}
