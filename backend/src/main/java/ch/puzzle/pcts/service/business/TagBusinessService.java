package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.service.persistence.TagPersistenceService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TagBusinessService {

    private final TagPersistenceService persistenceService;

    public TagBusinessService(TagPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Set<Tag> resolveTags(Set<Tag> rawTags) {
        return rawTags
                .stream()
                .map(tag -> persistenceService
                        .findWithIgnoreCase(tag.getName())
                        .orElseGet(() -> persistenceService.create(new Tag(null, tag.getName()))))
                .collect(Collectors.toSet());
    }
}
