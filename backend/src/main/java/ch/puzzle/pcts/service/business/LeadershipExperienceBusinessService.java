package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.LeadershipExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceBusinessService extends BusinessBase<LeadershipExperience> {
    private final LeadershipExperiencePersistenceService leadershipExperiencePersistenceService;

    LeadershipExperienceBusinessService(LeadershipExperiencePersistenceService persistenceService,
                                        LeadershipExperienceValidationService validationService) {
        super(validationService, persistenceService);

        leadershipExperiencePersistenceService = persistenceService;
    }

    public List<LeadershipExperience> getAll() {
        return persistenceService.getAll();
    }

    public List<LeadershipExperience> findAllByMember(Member member) {
        return leadershipExperiencePersistenceService.findAllByMember(member);
    }
}
