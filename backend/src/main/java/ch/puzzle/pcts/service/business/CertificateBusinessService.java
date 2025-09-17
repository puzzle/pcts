package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateBusinessService {
    private final CertificateValidationService validationService;
    private final CertificatePersistenceService persistenceService;

    public CertificateBusinessService(CertificateValidationService certificateValidationService,
                                      CertificatePersistenceService certificatePersistenceService) {
        this.validationService = certificateValidationService;
        this.persistenceService = certificatePersistenceService;
    }

    public Certificate create(Certificate certificate) {
        validationService.validateOnCreate(certificate);
        return persistenceService.create(certificate);
    }

    public Certificate getById(long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Certificate with id: " + id + "does not exist",
                                                     ErrorKey.NOT_FOUND));
    }

    public List<Certificate> getAll() {
        return persistenceService.getAll();
    }

    public Certificate update(Long id, Certificate certificate) {
        validationService.validateOnUpdate(id, certificate);
        return persistenceService.update(id, certificate);
    }
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }

}
