package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
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
    private CertificatePersistenceService persistenceService;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get leadershipExperience by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        long id = 1L;
        Certificate leadershipExperience = new Certificate(id,
                                                           "J+S",
                                                           BigDecimal.ONE,
                                                           "Comment",
                                                           CertificateType.YOUTH_AND_SPORT);
        when(persistenceService.getById(id)).thenReturn(Optional.of(leadershipExperience));

        Certificate result = businessService.getById(id);

        assertEquals(leadershipExperience, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
        verify(validationService).validateCertificateType(leadershipExperience.getCertificateType());
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
        when(persistenceService.getAllLeadershipExperiences()).thenReturn(leadershipExperiences);

        List<Certificate> result = businessService.getAll();

        assertArrayEquals(leadershipExperiences.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAllLeadershipExperiences();
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
