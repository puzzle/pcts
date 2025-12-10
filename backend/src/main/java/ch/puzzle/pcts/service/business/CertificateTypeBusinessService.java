package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeBusinessService extends BusinessBase<CertificateType> {
    private final TagBusinessService tagBusinessService;
    private final CertificateTypePersistenceService certificateTypePersistenceService;

    public CertificateTypeBusinessService(CertificateTypeValidationService certificateTypeValidationService,
                                          CertificateTypePersistenceService certificateTypePersistenceService,
                                          TagBusinessService tagBusinessService) {
        super(certificateTypeValidationService, certificateTypePersistenceService);
        this.certificateTypePersistenceService = certificateTypePersistenceService;
        this.tagBusinessService = tagBusinessService;
    }

    @Override
    public CertificateType create(CertificateType certificateType) {
        validationService.validateOnCreate(certificateType);

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));

        return persistenceService.save(certificateType);
    }

    public List<CertificateType> getAll() {
        return certificateTypePersistenceService.getAll();
    }

    @Override
    public CertificateType update(Long id, CertificateType certificateType) {
        validationService.validateOnUpdate(id, certificateType);
        certificateTypePersistenceService.getById(id);
        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));
        certificateType.setId(id);
        CertificateType result = persistenceService.save(certificateType);
        tagBusinessService.deleteUnusedTags();
        return result;
    }

    @Override
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        certificateTypePersistenceService.getById(id);
        persistenceService.delete(id);
        tagBusinessService.deleteUnusedTags();
    }

    @Override
    protected String entityName() {
        return CERTIFICATE_TYPE;
    }
}
