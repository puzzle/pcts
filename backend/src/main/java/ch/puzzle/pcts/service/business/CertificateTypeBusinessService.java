package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeBusinessService
        extends
            BusinessBase<CertificateType, CertificateTypeRepository, CertificateTypeValidationService, CertificateTypePersistenceService> {
    private final TagBusinessService tagBusinessService;

    public CertificateTypeBusinessService(CertificateTypeValidationService certificateTypeValidationService,
                                          CertificateTypePersistenceService certificateTypePersistenceService,
                                          TagBusinessService tagBusinessService) {
        super(certificateTypeValidationService, certificateTypePersistenceService);
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
            validationService.validateCertificateKind(certificateType.getCertificateKind());
            return certificateType;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    ErrorKey.NOT_FOUND,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                entityName(),
                                                FieldKey.FIELD,
                                                "id",
                                                FieldKey.IS,
                                                id.toString()));
        }
    }

    public List<CertificateType> getAll() {
        return persistenceService.getAllCertificateTypes();
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
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    ErrorKey.NOT_FOUND,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                entityName(),
                                                FieldKey.FIELD,
                                                "id",
                                                FieldKey.IS,
                                                id.toString()));
        }
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);

        if (persistenceService.getById(id).isPresent()) {
            persistenceService.delete(id);

            tagBusinessService.deleteUnusedTags();
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    ErrorKey.NOT_FOUND,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                entityName(),
                                                FieldKey.FIELD,
                                                "id",
                                                FieldKey.IS,
                                                id.toString()));
        }
    }

    @Override
    protected String entityName() {
        return CERTIFICATE_TYPE;
    }

}
