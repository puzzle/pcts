package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceBusinessService extends BusinessBase<Experience> {
    private final ExperiencePersistenceService experiencePersistenceService;

    public ExperienceBusinessService(ExperienceValidationService validationService,
                                     ExperiencePersistenceService persistenceService) {
        super(validationService, persistenceService);
        experiencePersistenceService = persistenceService;
    }

    public List<Experience> getAll() {
        return persistenceService.getAll();
    }

    public List<Experience> findAllByMember(Member member) {
        return experiencePersistenceService.findAllByMember(member);
    }
}
