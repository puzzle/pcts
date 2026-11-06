package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.EXPERIENCE;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.ExperienceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperiencePersistenceService extends PersistenceBase<Experience, ExperienceRepository> {
    public ExperiencePersistenceService(ExperienceRepository repository) {
        super(repository);
    }

    @Override
    public String entityName() {
        return EXPERIENCE;
    }

    public List<Experience> findAllByMember(Member member) {
        return repository.findAllByMember(member);
    }
}
