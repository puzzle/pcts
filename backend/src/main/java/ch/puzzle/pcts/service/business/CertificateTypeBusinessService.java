package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeBusinessService extends BusinessBase<CertificateType> {
    private final TagBusinessService tagBusinessService;
    private final CertificateTypePersistenceService certificateTypePersistenceService;

    @Value("${app.link-check.max-retries:3}")
    private int maxRetries;

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
        setMaxRetries(certificateType);

        return persistenceService.save(certificateType);
    }

    public List<CertificateType> getAll() {
        List<CertificateType> certificates = certificateTypePersistenceService.getAll();

        certificates.forEach(this::setMaxRetries);
        return certificateTypePersistenceService.getAll();
    }

    @Override
    public CertificateType update(Long id, CertificateType certificateType) {
        validationService.validateOnUpdate(id, certificateType);

        CertificateType existingCertificate = certificateTypePersistenceService.getById(id);

        setMaxRetries(certificateType);

        boolean linkHasChanged = !Objects.equals(existingCertificate.getLink(), certificateType.getLink());

        if (linkHasChanged) {
            certificateType.setLinkErrorCount(0);
        } else {
            certificateType
                    .keepLinkStatus(existingCertificate.getLinkErrorCount(),
                                    existingCertificate.getLinkLastCheckedAt());
        }

        certificateType.setTags(tagBusinessService.resolveTags(certificateType.getTags()));
        certificateType.setId(id);
        CertificateType result = persistenceService.save(certificateType);
        tagBusinessService.deleteUnusedTags();

        setMaxRetries(result);

        return result;
    }

    @Override
    public CertificateType getById(Long id) {
        setMaxRetries(certificateTypePersistenceService.getById(id));
        return super.getById(id);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
        tagBusinessService.deleteUnusedTags();
    }

    public List<CertificateType> findAllWhereLinkIsNotNull() {
        return certificateTypePersistenceService.findAllWhereLinkIsNotNull();
    }

    private void setMaxRetries(CertificateType certificateType) {
        certificateType.setMaxRetriesFromConfig(maxRetries);
    }
}
