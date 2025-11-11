package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeBusinessService {
    private final CertificateTypeValidationService validationService;
    private final CertificateTypePersistenceService persistenceService;
    private final TagBusinessService tagBusinessService;

    public CertificateTypeBusinessService(CertificateTypeValidationService certificateTypeValidationService,
                                          CertificateTypePersistenceService certificateTypePersistenceService,
                                          TagBusinessService tagBusinessService) {
        this.validationService = certificateTypeValidationService;
        this.persistenceService = certificateTypePersistenceService;
        this.tagBusinessService = tagBusinessService;
    }

    public CertificateType create(CertificateType certificateType) {
        validationService.validateOnCreate(certificateType);

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));

        return persistenceService.save(certificateType);
    }

    public CertificateType getById(Long id) {
        validationService.validateOnGetById(id);

        Optional<CertificateType> optionalCertificate = persistenceService.getById(id);

        if (optionalCertificate.isPresent()) {
            CertificateType certificateType = optionalCertificate.get();
            validationService.validateCertificateKind(certificateType.getCertificateKind());
            return certificateType;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    "Certificate type with id: " + id + " does not exist.",
                                    ErrorKey.NOT_FOUND);
        }
    }

    public List<CertificateType> getAll() {
        return persistenceService.getAllCertificateTypes();
    }

    public CertificateType update(Long id, CertificateType certificateType) {
        validationService.validateOnUpdate(id, certificateType);

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));
        certificateType.setId(id);

        CertificateType result = persistenceService.save(certificateType);
        tagBusinessService.deleteUnusedTags();

        return result;
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);

        persistenceService.delete(id);

        tagBusinessService.deleteUnusedTags();
    }

}
