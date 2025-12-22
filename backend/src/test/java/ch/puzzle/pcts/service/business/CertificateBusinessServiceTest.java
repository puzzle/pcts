package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private CertificateValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private Certificate certificate;

    @InjectMocks
    private CertificateBusinessService businessService;

    @DisplayName("Should get certificate by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(ID)).thenReturn(certificate);

        Certificate result = businessService.getById(ID);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should create certificate")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should update certificate")
    @Test
    void shouldUpdate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);
        when(persistenceService.getById(ID)).thenReturn((certificate));

        Certificate result = businessService.update(ID, certificate);

        assertEquals(certificate, result);
        verify(certificate).setId(ID);
        verify(validationService).validateOnUpdate(ID, certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        when(persistenceService.getById(ID)).thenReturn(certificate);
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }
}