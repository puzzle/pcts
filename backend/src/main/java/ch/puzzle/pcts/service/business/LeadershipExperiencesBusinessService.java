package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperiencesBusinessService extends BusinessBase<Certificate> {
    private final CertificatePersistenceService persistenceService;

    LeadershipExperiencesBusinessService(CertificatePersistenceService persistenceService,
                                         LeadershipExperienceValidationService validationService) {
        super(validationService, persistenceService);
        this.persistenceService = persistenceService;
    }

    public List<Certificate> getAll() {
        return this.persistenceService.findAllLeadershipExperience();
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE;
    }
}
