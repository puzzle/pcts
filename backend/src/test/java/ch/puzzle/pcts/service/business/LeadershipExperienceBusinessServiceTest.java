package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.LeadershipExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LeadershipExperienceBusinessServiceTest {

    @Mock
    private LeadershipExperienceValidationService validationService;

    @Mock
    private LeadershipExperiencePersistenceService persistenceService;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get leadershipExperience by id")
    @Test
    void shouldGetById() {
        Certificate leadershipExperience = new Certificate(1L,
                                                           "J+S",
                                                           BigDecimal.ONE,
                                                           "Comment",
                                                           CertificateType.YOUTH_AND_SPORT);
        when(persistenceService.getById(1L)).thenReturn(Optional.of(leadershipExperience));

        Certificate result = businessService.getById(1L);

        assertEquals(leadershipExperience, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception when role id is not found")
    @Test
    void shouldThrowExceptionWhenRoleIdNotFound() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Leadership experience with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should create leadershipExperience")
    @Test
    void shouldCreate() {
        Certificate leadershipExperience = new Certificate(1L,
                                                           "Military function",
                                                           BigDecimal.ONE,
                                                           "Comment",
                                                           CertificateType.MILITARY_FUNCTION);

        when(persistenceService.create(leadershipExperience)).thenReturn(leadershipExperience);

        Certificate result = businessService.create(leadershipExperience);

        assertEquals(leadershipExperience, result);
        verify(validationService).validateOnCreate(leadershipExperience);
        verify(persistenceService).create(leadershipExperience);
    }

    @DisplayName("Should get all leadershipExperiences")
    @Test
    void shouldGetAll() {
        List<Certificate> leadershipExperiences = List
                .of(new Certificate(1L,
                                    "Leadership training",
                                    BigDecimal.ONE,
                                    "Comment",
                                    CertificateType.LEADERSHIP_TRAINING),
                    new Certificate(2L,
                                    "Military function",
                                    BigDecimal.ONE,
                                    "Comment",
                                    CertificateType.MILITARY_FUNCTION));
        when(persistenceService.getAll()).thenReturn(leadershipExperiences);

        List<Certificate> result = businessService.getAll();

        assertArrayEquals(leadershipExperiences.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should update leadershipExperiences")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        Certificate leadershipExperience = new Certificate(1L,
                                                           "Leadership training",
                                                           BigDecimal.ONE,
                                                           "Comment",
                                                           CertificateType.LEADERSHIP_TRAINING);
        when(persistenceService.update(id, leadershipExperience)).thenReturn(leadershipExperience);

        Certificate result = businessService.update(id, leadershipExperience);

        assertEquals(leadershipExperience, result);
        verify(validationService).validateOnUpdate(id, leadershipExperience);
        verify(persistenceService).update(id, leadershipExperience);
    }

    @DisplayName("Should delete leadershipExperience")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }
}
