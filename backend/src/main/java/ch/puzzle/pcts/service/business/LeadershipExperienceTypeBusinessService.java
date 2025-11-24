package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeBusinessService
        extends
            BusinessBase<CertificateType, CertificateTypeRepository, LeadershipExperienceTypeValidationService, CertificateTypePersistenceService> {
    public LeadershipExperienceTypeBusinessService(LeadershipExperienceTypeValidationService leadershipExperienceTypeValidationService,
                                                   CertificateTypePersistenceService certificateTypePersistenceService) {
        super(leadershipExperienceTypeValidationService, certificateTypePersistenceService);
    }

    @Override
    public CertificateType getById(Long id) {
        validationService.validateOnGetById(id);

        Optional<CertificateType> optionalLeadershipExperienceType = persistenceService.getById(id);

        if (optionalLeadershipExperienceType.isPresent()) {
            CertificateType leadershipExperience = optionalLeadershipExperienceType.get();
            validationService.validateCertificateKind(leadershipExperience.getCertificateKind());
            return leadershipExperience;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.NOT_FOUND,
                                                                    Map
                                                                            .of(FieldKey.ENTITY,
                                                                                LEADERSHIP_EXPERIENCE_TYPE,
                                                                                FieldKey.FIELD,
                                                                                "id",
                                                                                FieldKey.IS,
                                                                                id.toString()))));
        }
    }

    public List<CertificateType> getAll() {
        return persistenceService.getAllLeadershipExperienceTypes();
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE_TYPE;
    }
}
