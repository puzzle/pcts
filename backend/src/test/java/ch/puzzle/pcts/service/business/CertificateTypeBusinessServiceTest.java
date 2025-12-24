package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateTypeBusinessServiceTest
        extends
            BaseBusinessTest<CertificateType, CertificateTypePersistenceService, CertificateTypeValidationService, CertificateTypeBusinessService> {

    @Mock
    private CertificateType certificateType;

    @Mock
    private List<CertificateType> certificateTypes;

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @Mock
    private CertificateTypeValidationService validationService;

    @Mock
    private TagBusinessService tagBusinessService;

    @InjectMocks
    private CertificateTypeBusinessService businessService;

    @Override
    CertificateType getModel() {
        return certificateType;
    }

    @Override
    CertificateTypePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    CertificateTypeValidationService getValidationService() {
        return validationService;
    }

    @Override
    CertificateTypeBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(certificateTypes);
        when(certificateTypes.size()).thenReturn(2);

        List<CertificateType> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(certificateTypes, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<CertificateType> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
