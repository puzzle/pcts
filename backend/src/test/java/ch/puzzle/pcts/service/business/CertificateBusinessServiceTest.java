package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateBusinessServiceTest {

    @Mock
    private CertificateValidationService validationService;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    private TagBusinessService tagBusinessService;

    @InjectMocks
    private CertificateBusinessService businessService;

    @DisplayName("Should get certificate by id and validate certificate type")
    @Test
    void shouldGetByIdAndValidateCertificateType() {
        long id = 1L;
        Certificate certificate = new Certificate(id,
                                                  "Master of Art",
                                                  BigDecimal.ONE,
                                                  "Comment",
                                                  Set.of(new Tag(1L, "Important tag")));
        when(persistenceService.getById(id)).thenReturn(Optional.of(certificate));

        Certificate result = businessService.getById(id);

        assertEquals(certificate, result);
        verify(validationService).validateOnGetById(id);
        verify(persistenceService).getById(id);
        verify(validationService).validateCertificateType(certificate.getCertificateType());
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
        when(persistenceService.getAllCertificates()).thenReturn(certificates);

        List<Certificate> result = businessService.getAll();

        assertArrayEquals(certificates.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAllCertificates();
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
