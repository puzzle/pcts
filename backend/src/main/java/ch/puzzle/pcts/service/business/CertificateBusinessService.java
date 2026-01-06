package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import org.springframework.stereotype.Service;

@Service
public class CertificateBusinessService extends BusinessBase<Certificate> {

    public CertificateBusinessService(CertificateValidationService validationService,
                                      CertificatePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }
}
