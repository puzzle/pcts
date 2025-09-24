package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagPersistenceService {

    private final TagRepository repository;

    @Autowired
    public TagPersistenceService(TagRepository repository) {
        this.repository = repository;
    }

    public Tag create(Tag tag) {
        return repository.save(tag);
    }

    public Optional<Tag> findWithIgnoreCase(String tagName) {
        return repository.findByNameIgnoreCase(tagName);
    }
}
