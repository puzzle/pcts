package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CERTIFICATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @DisplayName("Should throw error when certificate type with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, CERTIFICATE)),
                     exception.getErrorAttributes());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
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
        when(persistenceService.getById(ID)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.update(ID, certificate);

        assertEquals(certificate, result);
        verify(certificate).setId(ID);
        verify(validationService).validateOnUpdate(ID, certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should throw exception when updating non-existing certificate type")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, certificate));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete certificate")
    @Test
    void shouldDelete() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(certificate));
        businessService.delete(ID);


        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }

    @DisplayName("Should throw exception when deleting non-existing certificate type")
    @Test
    void shouldThrowExceptionWhenDeletingNotFoundCertificate() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}