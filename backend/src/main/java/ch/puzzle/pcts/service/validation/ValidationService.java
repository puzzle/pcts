package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.Model;

public interface ValidationService<T extends Model> {
    void validateOnGetById(Long id);

    void validateOnCreate(T model);

    void validateOnUpdate(Long id, T model);

    void validateOnDelete(Long id);

    void validate(T model);
}
