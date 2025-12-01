package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateRepository;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateBusinessService
        extends
            BusinessBase<Certificate, CertificateValidationService, CertificateRepository, CertificatePersistenceService> {

    public CertificateBusinessService(CertificateValidationService validationService,
                                      CertificatePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<Certificate> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return CERTIFICATE;
    }
}
