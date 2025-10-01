package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TagValidationServiceTest {

    TagValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new TagValidationService();
    }

    @DisplayName("Should trow exception when tag is null")
    @Test
    void tagIsNull() {
        assertThrows(PCTSException.class, () -> this.validationService.validateName(null));
    }

    @DisplayName("Should throw exception when name is null")
    @Test
    void nameIsNull() {
        assertThrows(PCTSException.class, () -> this.validationService.validateName(null));
    }
}
