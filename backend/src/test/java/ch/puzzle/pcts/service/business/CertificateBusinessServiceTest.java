package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.mode.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.Collections;
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
        when(persistenceService.getById(ID)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.getById(ID);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should throw exception when certificate not found by id")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(ID));

        assertEquals("Certificate with id: " + ID + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should get all certificates")
    @Test
    void shouldGetAll() {
        List<Certificate> expectedList = List.of(certificate, certificate);
        when(persistenceService.getAll()).thenReturn(expectedList);

        List<Certificate> result = businessService.getAll();

        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list when no certificates exist")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Certificate> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
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

        Certificate result = businessService.update(ID, certificate);

        assertEquals(certificate, result);
        verify(certificate).setId(ID);
        verify(validationService).validateOnUpdate(ID, certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }
}