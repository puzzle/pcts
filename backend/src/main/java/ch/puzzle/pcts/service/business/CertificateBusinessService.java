package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
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

    public List<Certificate> getAll() {

    }

    public Certificate getById(long id) {
        validationService.validateOnGetById(id);
    }

}
