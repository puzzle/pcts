package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;

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
public class CertificateTypeValidationService extends ValidationBase<CertificateType> {

    private final CertificateTypePersistenceService persistenceService;

    public CertificateTypeValidationService(CertificateTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnUpdate(Long id, CertificateType certificateType) {
        super.validateOnUpdate(id, certificateType);
        validateCertificateKind(certificateType.getCertificateKind());
        validateUniquenessOfNameAndPublisher(certificateType.getName(), certificateType.getPublisher());
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, certificateType.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CERTIFICATE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        certificateType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.INVALID_ARGUMENT, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }

    @Override
    public void validateOnCreate(CertificateType certificateType) {
        super.validateOnCreate(certificateType);
        validateCertificateKind(certificateType.getCertificateKind());
        validateUniquenessOfNameAndPublisher(certificateType.getName(), certificateType.getPublisher());
        if (UniqueNameValidationUtil.nameAlreadyUsed(certificateType.getName(), persistenceService::getByName)) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CERTIFICATE_TYPE,
                        FieldKey.FIELD,
                        "name",
                        FieldKey.IS,
                        certificateType.getName());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }

    public void validateCertificateKind(CertificateKind certificateKind) {
        if (certificateKind != CertificateKind.CERTIFICATE) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CERTIFICATE_TYPE,
                        FieldKey.FIELD,
                        "certificateKind",
                        FieldKey.IS,
                        certificateKind.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.ATTRIBUTE_WRONG_KIND, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));

        }
    }

    public void validateUniquenessOfNameAndPublisher(String name, String publisher) {
        if (persistenceService.existsByNameAndPublisher(name, publisher)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_UNIQUE,
                                                                    Map.of(FieldKey.FIELD, "name & publisher"))));
        }
    }
}
