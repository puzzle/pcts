package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateBusinessServiceTest
        extends
            BaseBusinessTest<Certificate, CertificatePersistenceService, CertificateValidationService, CertificateBusinessService> {

    @Mock
    Certificate certificate;

    @Mock
    CertificatePersistenceService persistenceService;

    @Mock
    CertificateValidationService validationService;

    @InjectMocks
    CertificateBusinessService businessService;

    @Override
    Certificate getModel() {
        return certificate;
    }

    @Override
    CertificatePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    CertificateValidationService getValidationService() {
        return validationService;
    }

    @Override
    CertificateBusinessService getBusinessService() {
        return businessService;
    }
}