package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeValidationService extends ValidationBase<CertificateType> {

    private final CertificateTypePersistenceService persistenceService;

    public LeadershipExperienceTypeValidationService(CertificateTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(CertificateType leadershipExperience) {
        super.validateOnCreate(leadershipExperience);
        validateCertificateKind(leadershipExperience.getCertificateKind());
        if (UniqueNameValidationUtil.nameAlreadyUsed(leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE,
                                                                    Map
                                                                            .of(FieldKey.ENTITY,
                                                                                LEADERSHIP_EXPERIENCE_TYPE,
                                                                                FieldKey.FIELD,
                                                                                "name",
                                                                                FieldKey.IS,
                                                                                leadershipExperience.getName()))));
        }
    }

    @Override
    public void validateOnUpdate(Long id, CertificateType leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        validateCertificateKind(leadershipExperience.getCertificateKind());
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE,
                                                                    Map
                                                                            .of(FieldKey.ENTITY,
                                                                                LEADERSHIP_EXPERIENCE_TYPE,
                                                                                FieldKey.FIELD,
                                                                                "name",
                                                                                FieldKey.IS,
                                                                                leadershipExperience.getName()))));
        }
    }

    public void validateCertificateKind(CertificateKind certificatekind) {
        if (!certificatekind.isLeadershipExperienceType()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.INVALID_ARGUMENT,
                                                                    Map
                                                                            .of(FieldKey.ENTITY,
                                                                                LEADERSHIP_EXPERIENCE_TYPE,
                                                                                FieldKey.FIELD,
                                                                                "certificateKind",
                                                                                FieldKey.IS,
                                                                                certificatekind.toString()))));
        }
    }
}
