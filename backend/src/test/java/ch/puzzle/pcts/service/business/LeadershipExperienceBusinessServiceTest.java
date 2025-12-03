package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
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
class LeadershipExperienceBusinessServiceTest {
    private static final Long ID = 1L;

    @Mock
    private LeadershipExperienceValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private Certificate leadershipExperience;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @DisplayName("Should get leadership experience by id")
    @Test
    void shouldGetLeadershipExperienceById() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.of(leadershipExperience));

        Certificate result = businessService.getById(ID);

        assertEquals(leadershipExperience, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).findLeadershipExperience(ID);
    }

    @DisplayName("Should throw error when leadership experience with id does not exist")
    @Test
    void shouldNotGetLeadershipExperienceByIdAndThrowError() {
        Long id = 1L;
        when(persistenceService.findLeadershipExperience(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, LEADERSHIP_EXPERIENCE)),
                     exception.getErrorAttributes());
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).findLeadershipExperience(id);
    }

    @DisplayName("Should delete leadership experience")
    @Test
    void shouldDeleteLeadershipExperience() {
        when(persistenceService.findLeadershipExperience(ID)).thenReturn(Optional.of(leadershipExperience));
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }

    @DisplayName("Should throw exception when deleting non-existing leadership experience")
    @Test
    void shouldThrowExceptionWhenDeletingNotFoundLeadershipExperience() {
        Long id = 1L;
        when(persistenceService.findLeadershipExperience(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).findLeadershipExperience(id);
        verify(persistenceService, never()).delete(id);
    }
}
