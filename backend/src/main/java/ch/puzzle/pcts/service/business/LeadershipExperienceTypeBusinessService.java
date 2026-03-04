package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.leadershipexperiencetype.LeadershipExperienceType;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeBusinessService extends BusinessBase<LeadershipExperienceType> {

    private final LeadershipExperienceTypePersistenceService leadershipTypePersistenceService;

    public LeadershipExperienceTypeBusinessService(LeadershipExperienceTypeValidationService leadershipExperienceTypeValidationService,
                                                   LeadershipExperienceTypePersistenceService leadershipTypePersistenceService) {
        super(leadershipExperienceTypeValidationService, leadershipTypePersistenceService);
        this.leadershipTypePersistenceService = leadershipTypePersistenceService;
    }

    public List<LeadershipExperienceType> getAll() {
        return leadershipTypePersistenceService.getAll();
    }
}
