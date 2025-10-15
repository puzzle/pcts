package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Tag;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TagPersistenceServiceIT extends PersistenceCoreIT {

    @Autowired
    private TagPersistenceService persistenceService;

    @DisplayName("Should create tag")
    @Test
    void shouldCreate() {
        Tag tag = new Tag(null, "Very important tag");

        Tag result = persistenceService.create(tag);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(tag.getName());
    }

    @DisplayName("Should find tag by name written in different cases")
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = { "Longer tag name", "longer tag name", "LONGER TAG NAME", "loNGER tAg NAme" })
    void shouldFindTagWithDifferentCases(String tagName) {
        Tag result = persistenceService.findWithIgnoreCase(tagName).get();

        assertThat(result.getName()).isEqualTo("Longer tag name");
        assertThat(result.getId()).isEqualTo(2L);
    }
}
