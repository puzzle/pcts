package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
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
class LeadershipExperienceTypeBusinessServiceTest {

    @Mock
    private LeadershipExperienceTypeValidationService validationService;

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @Mock
    private CertificateType certificate;

    @Mock
    private List<CertificateType> certificates;

    @InjectMocks
    private LeadershipExperienceTypeBusinessService businessService;

    @DisplayName("Should get leadershipExperience by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));
        when(certificate.getCertificateKind()).thenReturn(CertificateKind.YOUTH_AND_SPORT);

        CertificateType result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
    }

    @DisplayName("Should throw error when certificate type with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowErrorIfCertificateKindIsCertificate() {
        Long id = 1L;
        when(certificate.getCertificateKind()).thenReturn(CertificateKind.CERTIFICATE);
        when(certificate.getId()).thenReturn(id);
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "id",
                            FieldKey.IS,
                            id.toString(),
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
    }

    @DisplayName("Should throw error when leadership experience type with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "id",
                            FieldKey.IS,
                            id.toString(),
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
    }

    @DisplayName("Should create leadershipExperience type")
    @Test
    void shouldCreate() {
        when(persistenceService.save(certificate)).thenReturn(certificate);

        CertificateType result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).save(certificate);
    }

    @DisplayName("Should get all leadershipExperience type")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAllLeadershipExperienceTypes()).thenReturn(certificates);
        when(certificates.size()).thenReturn(2);

        List<CertificateType> result = businessService.getAll();

        assertEquals(certificates, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAllLeadershipExperienceTypes();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should update leadershipExperience type")
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
    }

    @DisplayName("Should throw exception when updating non-existing leadership experience type")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, certificate));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete leadershipExperience type")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing leadership experience type")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}
