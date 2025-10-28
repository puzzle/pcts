package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateBusinessServiceTest {

    @Mock
    private CertificateValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private TagBusinessService tagBusinessService;

    @Mock
    private Certificate certificate;

    @Mock
    private List<Certificate> certificates;

    @InjectMocks
    private CertificateBusinessService businessService;

    @DisplayName("Should get certificate by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
        verify(validationService).validateCertificateType(certificate.getCertificateType());
    }

    @DisplayName("Should throw error when certificate with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals("Certificate with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
    }

    @DisplayName("Should create certificate")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
        verify(tagBusinessService).resolveTags(any());
    }

    @DisplayName("Should get all certificates")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAllCertificates()).thenReturn(certificates);
        when(certificates.size()).thenReturn(2);

        List<Certificate> result = businessService.getAll();

        assertEquals(certificates, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAllCertificates();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should update certificates")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(certificate)).thenReturn(certificate);

        Certificate result = businessService.update(id, certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnUpdate(id, certificate);
        verify(certificate).setId(id);
        verify(persistenceService).save(certificate);
        verify(tagBusinessService).deleteUnusedTags();
    }

    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
        verify(tagBusinessService).deleteUnusedTags();
    }
}
