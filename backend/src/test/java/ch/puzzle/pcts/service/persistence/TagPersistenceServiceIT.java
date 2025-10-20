package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
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
        return List.of(new Tag(1L, "Tag 1"), new Tag(2L, "Longer tag name"));
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
