package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.TagRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TagBusinessService {

    private final TagRepository tagRepository;

    public TagBusinessService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> resolveTags(Set<Tag> rawTags) {
        return rawTags
                .stream()
                .map(tag -> tagRepository
                        .findByNameIgnoreCase(tag.getName())
                        .orElseGet(() -> tagRepository.save(new Tag(null, tag.getName()))))
                .collect(Collectors.toSet());
    }
}
