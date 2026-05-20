package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.TAG;

import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TagPersistenceService extends PersistenceBase<Tag, TagRepository> {
    private final TagRepository tagRepository;

    public TagPersistenceService(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    public String entityName() {
        return TAG;
    }

    public Optional<Tag> findWithIgnoreCase(String tagName) {
        return tagRepository.findByNameIgnoreCase(tagName);
    }

    public Set<Tag> findAllUnusedTags() {
        return tagRepository.findAllUnusedTags();
    }

    public void deleteAll(Set<Tag> tags) {
        tagRepository.deleteAll(tags);
    }
}
