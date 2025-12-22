package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.service.persistence.TagPersistenceService;
import ch.puzzle.pcts.service.validation.TagValidationService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TagBusinessService {

    private final TagPersistenceService persistenceService;
    private final TagValidationService validationService;

    public TagBusinessService(TagPersistenceService persistenceService, TagValidationService validationService) {
        this.persistenceService = persistenceService;
        this.validationService = validationService;
    }

    public Set<Tag> resolveTags(Set<Tag> rawTags) {
        if (rawTags == null || rawTags.isEmpty()) {
            return Set.of();
        }

        return rawTags.stream().map(tag -> {
            validationService.validate(tag);
            return persistenceService
                    .findWithIgnoreCase(tag.getName())
                    .orElseGet(() -> persistenceService.save(new Tag(null, tag.getName())));
        }).collect(Collectors.toSet());
    }

    public void deleteUnusedTags() {
        Set<Tag> unusedTags = persistenceService.findAllUnusedTags();

        persistenceService.deleteAll(unusedTags);
    }
}
