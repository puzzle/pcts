package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeBusinessService extends BusinessBase<CertificateType> {

    private final CertificateTypePersistenceService certificateTypePersistenceService;

    public LeadershipExperienceTypeBusinessService(LeadershipExperienceTypeValidationService leadershipExperienceTypeValidationService,
                                                   CertificateTypePersistenceService certificateTypePersistenceService) {
        super(leadershipExperienceTypeValidationService, certificateTypePersistenceService);
        this.certificateTypePersistenceService = certificateTypePersistenceService;
    }

    @Override
    public CertificateType getById(Long id) {
        validationService.validateOnGetById(id);
        return certificateTypePersistenceService.getLeadershipExperienceType(id);
    }

    public List<CertificateType> getAll() {
        return certificateTypePersistenceService.getAllLeadershipExperienceTypes();
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE_TYPE;
    }
}
