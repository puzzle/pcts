package ch.puzzle.pcts.service.validation.util;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.model.Model;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UniqueNameValidationUtilTest {

    static class FakeModel implements Model {
        Long id;
        String name;

        FakeModel(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }
    }

    @Test
    @DisplayName("Should return true when name already exists")
    void shouldReturnTrueWhenNameAlreadyExists() {
        boolean result = UniqueNameValidationUtil.nameAlreadyUsed("Test", name -> Optional.of(new FakeModel(1L, name)));

        assertTrue(result, "Expected true when name already exists");
    }

    @Test
    @DisplayName("Should return false when name is unique")
    void shouldReturnFalseWhenNameIsUnique() {
        boolean result = UniqueNameValidationUtil.nameAlreadyUsed("UniqueName", name -> Optional.empty());

        assertFalse(result, "Expected false when name is unique");
    }

    @Test
    @DisplayName("Should return true when another entity has the same name")
    void shouldReturnTrueWhenOtherEntityHasSameName() {
        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "Duplicate", name -> Optional.of(new FakeModel(2L, name)));

        assertTrue(result, "Expected true when another entity has the same name");
    }

    @Test
    @DisplayName("Should return false when entity with same ID has same name")
    void shouldReturnFalseWhenSameEntityHasSameName() {
        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "SameName", name -> Optional.of(new FakeModel(1L, name)));

        assertFalse(result, "Expected false when same entity has same name");
    }

    @Test
    @DisplayName("Should return false when no entity found")
    void shouldReturnFalseWhenNoEntityFound() {
        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "NonExistent", name -> Optional.empty());

        assertFalse(result, "Expected false when no entity found");
    }
}
