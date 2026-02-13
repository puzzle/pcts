package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateTypeValidationService;
import java.time.LocalDateTime;
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
        verify(persistenceService, times(2)).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<CertificateType> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @Override
    @DisplayName("Should get model by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(1L)).thenReturn(certificateType);

        CertificateType result = businessService.getById(1L);

        assertEquals(certificateType, result);

        verify(validationService).validateOnGetById(1L);
        verify(persistenceService, times(2)).getById(1L);
    }

    @DisplayName("Should keep link_error_count and last_checked_at when link is not changed on update")
    @Test
    void updateShouldKeepErrorCount() {
        Long id = 1L;
        String link = "https://example.com";
        int existingErrorCount = 5;
        LocalDateTime existingLastCheckedAt = mock(LocalDateTime.class);

        CertificateType existing = mock(CertificateType.class);
        CertificateType updated = mock(CertificateType.class);

        when(existing.getLink()).thenReturn(link);
        when(updated.getLink()).thenReturn(link);
        when(existing.getLinkErrorCount()).thenReturn(existingErrorCount);
        when(existing.getLinkLastCheckedAt()).thenReturn(existingLastCheckedAt);

        when(persistenceService.getById(id)).thenReturn(existing);
        when(persistenceService.save(updated)).thenReturn(updated);

        CertificateType result = businessService.update(id, updated);

        verify(updated).keepLinkStatus(existingErrorCount, existingLastCheckedAt);

        verify(updated).setId(id);
        verify(persistenceService).save(updated);

        verify(tagBusinessService).resolveTags(updated.getTags());
        verify(tagBusinessService).deleteUnusedTags();

        assertEquals(updated, result);
    }

    @DisplayName("Should reset link_error_count to 0 when link is changed on update")
    @Test
    void updateShouldResetErrorCountWhenLinkChanges() {
        Long id = 1L;
        String oldLink = "https://example.com";
        String newLink = "https://new-example.com";

        CertificateType existing = mock(CertificateType.class);
        CertificateType updated = mock(CertificateType.class);

        when(existing.getLink()).thenReturn(oldLink);
        when(updated.getLink()).thenReturn(newLink);

        when(persistenceService.getById(id)).thenReturn(existing);
        when(persistenceService.save(updated)).thenReturn(updated);

        CertificateType result = businessService.update(id, updated);

        verify(updated).setLinkErrorCount(0);

        verify(updated, never()).keepLinkStatus(anyInt(), any());

        verify(updated).setId(id);
        verify(persistenceService).save(updated);
        verify(tagBusinessService).resolveTags(updated.getTags());
        verify(tagBusinessService).deleteUnusedTags();

        assertEquals(updated, result);
    }

    @DisplayName("Should delegate findAllWhereLinkIsNotNull to persistence service")
    @Test
    void shouldDelegateFindAllWhereLinkIsNotNull() {
        List<CertificateType> mockResult = Collections.emptyList();
        when(persistenceService.findAllWhereLinkIsNotNull()).thenReturn(mockResult);

        List<CertificateType> result = businessService.findAllWhereLinkIsNotNull();

        assertEquals(mockResult, result);
        verify(persistenceService).findAllWhereLinkIsNotNull();
    }

}
