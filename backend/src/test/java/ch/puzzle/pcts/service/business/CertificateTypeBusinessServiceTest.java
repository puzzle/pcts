package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateTypeBusinessServiceTest {

    @Mock
    private CertificateTypeValidationService validationService;

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @Mock
    private TagBusinessService tagBusinessService;

    @Mock
    private CertificateType certificate;

    @Mock
    private List<CertificateType> certificates;

    @InjectMocks
    private CertificateTypeBusinessService businessService;

    @DisplayName("Should get certificate type by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        CertificateType result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
    }

    @DisplayName("Should create certificate type")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        CertificateType result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
        verify(tagBusinessService).resolveTags(any());
    }

    @DisplayName("Should get all certificate types")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(certificates);
        when(certificates.size()).thenReturn(2);

        List<CertificateType> result = businessService.getAll();

        assertEquals(certificates, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should update certificate type")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(certificate)).thenReturn(certificate);
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        CertificateType result = businessService.update(id, certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnUpdate(id, certificate);
        verify(certificate).setId(id);
        verify(persistenceService).save(certificate);
        verify(tagBusinessService).deleteUnusedTags();
    }

    @DisplayName("Should delete certificate type")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
        verify(tagBusinessService).deleteUnusedTags();
    }
}
