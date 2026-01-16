package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.TAG_2_ID;
import static ch.puzzle.pcts.util.TestDataModels.TAGS_1;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TagPersistenceServiceIT extends PersistenceBaseIT<Tag, TagRepository, TagPersistenceService> {

    private final TagPersistenceService service;

    @Autowired
    TagPersistenceServiceIT(TagPersistenceService service) {
        super(service);
        this.service = service;
    }

    @Override
    Tag getModel() {
        return new Tag(null, "Very important tag");
    }

    List<Tag> getAll() {
        return TAGS_1;
    }

    @DisplayName("Should find tag by name written in different cases")
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = { "Longer tag name", "longer tag name", "LONGER TAG NAME", "loNGER tAg NAme" })
    void shouldFindTagWithDifferentCases(String tagName) {
        Tag result = service.findWithIgnoreCase(tagName).get();

        assertThat(result.getName()).isEqualTo("Longer tag name");
        assertThat(result.getId()).isEqualTo(TAG_2_ID);
    }
}
