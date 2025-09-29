package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TagPersistenceService {

    private final TagRepository repository;

    public TagPersistenceService(TagRepository repository) {
        this.repository = repository;
    }

    public Tag create(Tag tag) {
        return repository.save(tag);
    }

    public Optional<Tag> findWithIgnoreCase(String tagName) {
        return repository.findByNameIgnoreCase(tagName);
    }

    public Optional<Tag> getById(Long id) {
        return repository.findById(id);
    }

    public Set<Tag> findAllUnusedTags() {
        return repository.findAllUnusedTags();
    }

    public void delete(Set<Tag> tags) {
        repository.deleteAll(tags);
    }
}
