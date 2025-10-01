package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CertificateBusinessServiceTest {

    @Mock
    private CertificateValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private TagBusinessService tagBusinessService;

    @InjectMocks
    private CertificateBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get certificate by id")
    @Test
    void shouldGetById() {
        Certificate certificate = new Certificate(1L,
                                                  "Master of Art",
                                                  BigDecimal.ONE,
                                                  "Comment",
                                                  Set.of(new Tag(1L, "Important tag")));
        when(persistenceService.getById(1L)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.getById(1L);

        assertEquals(certificate, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception when role id is not found")
    @Test
    void shouldThrowExceptionWhenRoleIdNotFound() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Certificate with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should create certificate")
    @Test
    void shouldCreate() {
        Certificate certificate = new Certificate(1L,
                                                  "Bachelor of Business Administration",
                                                  BigDecimal.ONE,
                                                  "Comment",
                                                  Set.of(new Tag(1L, "Important tag")));
        when(persistenceService.create(certificate)).thenReturn(certificate);

        Certificate result = businessService.create(certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnCreate(certificate);
        verify(persistenceService).create(certificate);

        verify(tagBusinessService).resolveTags(any());
    }

    @DisplayName("Should get all certificates")
    @Test
    void shouldGetAll() {
        List<Certificate> certificates = List
                .of(new Certificate(1L,
                                    "Bachelor of Sciences",
                                    BigDecimal.ONE,
                                    "Comment",
                                    Set.of(new Tag(1L, "Important tag"))),
                    new Certificate(2L,
                                    "Master of Business Administration",
                                    BigDecimal.ONE,
                                    "Comment",
                                    Set.of(new Tag(1L, "Important tag"))));
        when(persistenceService.getAll()).thenReturn(certificates);

        List<Certificate> result = businessService.getAll();

        assertArrayEquals(certificates.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should update certificates")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        Certificate certificate = new Certificate(1L,
                                                  "Certificate in Advanced English",
                                                  BigDecimal.ONE,
                                                  "Comment",
                                                  Set.of(new Tag(1L, "Important tag")));
        when(persistenceService.update(id, certificate)).thenReturn(certificate);

        Certificate result = businessService.update(id, certificate);

        assertEquals(certificate, result);
        verify(validationService).validateOnUpdate(id, certificate);
        verify(persistenceService).update(id, certificate);

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
