package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.TAG;

import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TagPersistenceService extends PersistenceBase<Tag, TagRepository> {

    private final TagRepository repository;

    public TagPersistenceService(TagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public String entityName() {
        return TAG;
    }

    public Optional<Tag> findWithIgnoreCase(String tagName) {
        return repository.findByNameIgnoreCase(tagName);
    }

    public Set<Tag> findAllUnusedTags() {
        return repository.findAllUnusedTags();
    }

    public void deleteAll(Set<Tag> tags) {
        repository.deleteAll(tags);
    }
}
