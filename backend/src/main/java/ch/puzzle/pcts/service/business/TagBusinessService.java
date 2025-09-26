package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Tag;
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
        return rawTags.stream().map(tag -> {
            validationService.validateName(tag);
            return persistenceService
                    .findWithIgnoreCase(tag.getName())
                    .orElseGet(() -> persistenceService.create(new Tag(null, tag.getName())));
        }).collect(Collectors.toSet());
    }
}
