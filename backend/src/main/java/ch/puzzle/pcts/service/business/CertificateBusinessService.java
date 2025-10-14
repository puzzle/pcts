package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateBusinessService {
    private final CertificateValidationService validationService;
    private final CertificatePersistenceService persistenceService;
    private final TagBusinessService tagBusinessService;

    public CertificateBusinessService(CertificateValidationService certificateValidationService,
                                      CertificatePersistenceService certificatePersistenceService,
                                      TagBusinessService tagBusinessService) {
        this.validationService = certificateValidationService;
        this.persistenceService = certificatePersistenceService;
        this.tagBusinessService = tagBusinessService;
    }

    public Certificate create(Certificate certificate) {
        validationService.validateOnCreate(certificate);

        certificate.setTags(tagBusinessService.resolveTags(certificate.getTags()));

        return persistenceService.create(certificate);
    }

    public Certificate getById(Long id) {
        validationService.validateOnGetById(id);

        Optional<Certificate> optionalCertificate = persistenceService.getById(id);

        if (optionalCertificate.isPresent()) {
            Certificate certificate = optionalCertificate.get();
            validationService.validateCertificateType(certificate.getCertificateType());
            return certificate;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    "Certificate with id: " + id + " does not exist.",
                                    ErrorKey.NOT_FOUND);
        }
    }

    public List<Certificate> getAll() {
        return persistenceService.getAllCertificates();
    }

    public Certificate update(Long id, Certificate certificate) {
        validationService.validateOnUpdate(id, certificate);

        certificate.setTags(tagBusinessService.resolveTags(certificate.getTags()));

        Certificate result = persistenceService.update(id, certificate);

        tagBusinessService.deleteUnusedTags();

        return result;
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);

        persistenceService.delete(id);

        tagBusinessService.deleteUnusedTags();
    }

}
