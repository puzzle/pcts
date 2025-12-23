package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.LeadershipTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeBusinessService extends BusinessBase<CertificateType> {

    private final LeadershipTypePersistenceService leadershipTypePersistenceService;

    public LeadershipExperienceTypeBusinessService(LeadershipExperienceTypeValidationService leadershipExperienceTypeValidationService,
                                                   LeadershipTypePersistenceService leadershipTypePersistenceService) {
        super(leadershipExperienceTypeValidationService, leadershipTypePersistenceService);
        this.leadershipTypePersistenceService = leadershipTypePersistenceService;
    }

    public List<CertificateType> getAll() {
        return leadershipTypePersistenceService.getAll();
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        leadershipTypePersistenceService.getById(id);
        leadershipTypePersistenceService.delete(id);
    }
}
