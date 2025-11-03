package ch.puzzle.pcts.service.validation.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.Model;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UniqueNameValidationUtilTest {

    @Mock
    private Model mockModel;

    @Test
    @DisplayName("Should return true when name already exists")
    void shouldReturnTrueWhenNameAlreadyExists() {
        boolean result = UniqueNameValidationUtil.nameAlreadyUsed("Test", name -> Optional.of(mockModel));

        assertTrue(result, "Expected true when name already exists");
    }

    @Test
    @DisplayName("Should return false when name is unique")
    void shouldReturnFalseWhenNameIsUnique() {
        boolean result = UniqueNameValidationUtil.nameAlreadyUsed("UniqueName", name -> Optional.empty());
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when another entity has the same name")
    void shouldReturnTrueWhenOtherEntityHasSameName() {
        when(mockModel.getId()).thenReturn(2L);

        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "Duplicate", name -> Optional.of(mockModel));

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when entity with same ID has same name")
    void shouldReturnFalseWhenSameEntityHasSameName() {
        when(mockModel.getId()).thenReturn(1L);

        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "SameName", name -> Optional.of(mockModel));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when no entity found")
    void shouldReturnFalseWhenNoEntityFound() {
        boolean result = UniqueNameValidationUtil
                .nameExcludingSelfAlreadyUsed(1L, "NonExistent", name -> Optional.empty());

        assertFalse(result);
    }
}
