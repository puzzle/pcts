package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.service.persistence.MemberCertificatePersistenceService;
import ch.puzzle.pcts.service.validation.MemberCertificateValidationService;
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
class MemberCertificateBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private MemberCertificateValidationService validationService;

    @Mock
    private MemberCertificatePersistenceService persistenceService;

    @Mock
    private MemberCertificate memberCertificate;

    @InjectMocks
    private MemberCertificateBusinessService businessService;

    @DisplayName("Should get member certificate by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(memberCertificate));

        MemberCertificate result = businessService.getById(ID);

        assertEquals(memberCertificate, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should throw exception when member certificate not found by id")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(ID));

        assertEquals("Member certificate with id: " + ID + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should get all member certificates")
    @Test
    void shouldGetAll() {
        List<MemberCertificate> expectedList = List.of(memberCertificate, memberCertificate);
        when(persistenceService.getAll()).thenReturn(expectedList);

        List<MemberCertificate> result = businessService.getAll();

        assertEquals(expectedList.size(), result.size());
        assertEquals(expectedList, result);
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list when no member certificates exist")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<MemberCertificate> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create member certificate")
    @Test
    void shouldCreate() {
        when(persistenceService.save(memberCertificate)).thenReturn(memberCertificate);

        MemberCertificate result = businessService.create(memberCertificate);

        assertEquals(memberCertificate, result);
        verify(validationService).validateOnCreate(memberCertificate);
        verify(persistenceService).save(memberCertificate);
    }

    @DisplayName("Should update member certificate")
    @Test
    void shouldUpdate() {
        when(persistenceService.save(memberCertificate)).thenReturn(memberCertificate);

        MemberCertificate result = businessService.update(ID, memberCertificate);

        assertEquals(memberCertificate, result);
        verify(memberCertificate).setId(ID);
        verify(validationService).validateOnUpdate(ID, memberCertificate);
        verify(persistenceService).save(memberCertificate);
    }

    @DisplayName("Should delete member certificate")
    @Test
    void shouldDelete() {
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }
}