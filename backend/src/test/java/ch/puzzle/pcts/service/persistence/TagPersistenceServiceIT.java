package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TagPersistenceServiceIT extends PersistenceBaseIT<Tag, TagRepository, TagPersistenceService> {

    private final TagPersistenceService service;

    TagPersistenceServiceIT(TagPersistenceService service) {
        super(service);
        this.service = service;
    }

    @Override
    Tag getCreateEntity() {
        return new Tag(null, "Very important tag");
    }

    @Override
    Tag getUpdateEntity() {
        return new Tag(null, "Updated tag");
    }

    @Override
    Long getId(Tag tag) {
        return tag.getId();
    }

    @Override
    void setId(Tag tag, Long id) {
        tag.setId(id);
    }

    @DisplayName("Should find tag by name written in different cases")
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = { "Longer tag name", "longer tag name", "LONGER TAG NAME", "loNGER tAg NAme" })
    @Order(2)
    void shouldFindTagWithDifferentCases(String tagName) {
        Tag result = service.findWithIgnoreCase(tagName).get();

        assertThat(result.getName()).isEqualTo("Longer tag name");
        assertThat(result.getId()).isEqualTo(2L);
    }
}
