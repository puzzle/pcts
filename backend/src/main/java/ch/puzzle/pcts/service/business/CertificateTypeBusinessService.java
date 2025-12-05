package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeBusinessService extends BusinessBase<CertificateType> {
    private final TagBusinessService tagBusinessService;
    private final CertificateTypePersistenceService certificateTypePersistenceService;
    private final CertificateTypeValidationService certificateTypeValidationService;

    public CertificateTypeBusinessService(CertificateTypeValidationService certificateTypeValidationService,
                                          CertificateTypePersistenceService certificateTypePersistenceService,
                                          TagBusinessService tagBusinessService) {
        super(certificateTypeValidationService, certificateTypePersistenceService);
        this.certificateTypePersistenceService = certificateTypePersistenceService;
        this.certificateTypeValidationService = certificateTypeValidationService;
        this.tagBusinessService = tagBusinessService;
    }

    @Override
    public CertificateType create(CertificateType certificateType) {
        validationService.validateOnCreate(certificateType);

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));

        return persistenceService.save(certificateType);
    }

    @Override
    public CertificateType getById(Long id) {
        validationService.validateOnGetById(id);

        Optional<CertificateType> optionalCertificate = persistenceService.getById(id);

        if (optionalCertificate.isPresent()) {
            CertificateType certificateType = optionalCertificate.get();
            certificateTypeValidationService.validateCertificateKind(certificateType.getCertificateKind());
            return certificateType;
        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));

        }
    }

    public List<CertificateType> getAll() {
        return certificateTypePersistenceService.getAllCertificateTypes();
    }

    @Override
    public CertificateType update(Long id, CertificateType certificateType) {
        validationService.validateOnUpdate(id, certificateType);

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));
        if (persistenceService.getById(id).isPresent()) {

            certificateType.setId(id);

            CertificateType result = persistenceService.save(certificateType);
            tagBusinessService.deleteUnusedTags();

            return result;
        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));

        }
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);

        if (persistenceService.getById(id).isPresent()) {
            persistenceService.delete(id);

            tagBusinessService.deleteUnusedTags();
        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));

        }
    }

    @Override
    protected String entityName() {
        return CERTIFICATE_TYPE;
    }

}
