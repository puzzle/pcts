package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
        return certificatePersistenceService.findLeadershipExperience(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);

        if (certificatePersistenceService.findLeadershipExperience(id).isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, "leadershipExperience", FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }

        certificatePersistenceService.delete(id);
    }

    @Override
    protected String entityName() {
        return LEADERSHIP_EXPERIENCE;
    }
}
