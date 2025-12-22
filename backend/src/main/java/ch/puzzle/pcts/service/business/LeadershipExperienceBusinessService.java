package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceBusinessService extends BusinessBase<Certificate> {
    private final CertificatePersistenceService certificatePersistenceService;

    LeadershipExperienceBusinessService(CertificatePersistenceService certificatePersistenceService,
                                        LeadershipExperienceValidationService validationService) {
        super(validationService, certificatePersistenceService);
        this.certificatePersistenceService = certificatePersistenceService;
    }

    @Override
    public Certificate getById(Long id) {
        validationService.validateOnGetById(id);
        return certificatePersistenceService.findLeadershipExperience(id);
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        certificatePersistenceService.findLeadershipExperience(id);
        certificatePersistenceService.delete(id);
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE;
    }
}
