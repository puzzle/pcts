package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CertificateCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CertificateCalculationValidationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
class CertificateCalculationBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private CertificateCalculationValidationService validationService;

    @Mock
    private CertificateCalculationPersistenceService persistenceService;

    @InjectMocks
    private CertificateCalculationBusinessService businessService;

    static Stream<Arguments> certificatePointsProvider() {
        return Stream
                .of(Arguments.of(false, CertificateKind.CERTIFICATE, BigDecimal.TEN, BigDecimal.TEN),
                    Arguments.of(false, CertificateKind.CERTIFICATE, BigDecimal.TEN, BigDecimal.TEN),
                    Arguments.of(true, CertificateKind.MILITARY_FUNCTION, BigDecimal.ONE, BigDecimal.ONE),
                    Arguments.of(false, CertificateKind.LEADERSHIP_TRAINING, BigDecimal.TWO, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should get certificate calculations by calculation id")
    void shouldGetByCalculationId() {
        CertificateCalculation cc = mock(CertificateCalculation.class);
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(cc));

        List<CertificateCalculation> result = businessService.getByCalculationId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    @DisplayName("Should get certificate calculations by certificate id")
    void shouldGetByCertificateId() {
        CertificateCalculation cc = mock(CertificateCalculation.class);
        when(persistenceService.getByCertificateId(ID)).thenReturn(List.of(cc));

        List<CertificateCalculation> result = businessService.getByCertificateId(ID);

        assertEquals(1, result.size());
        verify(persistenceService).getByCertificateId(ID);
    }

    /*
     * The strictness of Mockito is less here because depending on params some
     * stubbing's are unnecessary which causes a mockito error
     */
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Should calculate certificate points correctly")
    @ParameterizedTest
    @MethodSource("certificatePointsProvider")
    void shouldCalculateCertificatePoints(boolean isManagement, CertificateKind kind, BigDecimal certificatePoints,
                                          BigDecimal expectedResult) {
        Calculation calculation = mock(Calculation.class);
        Role role = mock(Role.class);

        when(calculation.getRole()).thenReturn(role);
        when(role.getIsManagement()).thenReturn(isManagement);

        CertificateType type = mock(CertificateType.class);
        when(type.getCertificateKind()).thenReturn(kind);
        when(type.getPoints()).thenReturn(certificatePoints);

        Certificate certificate = mock(Certificate.class);
        when(certificate.getCertificateType()).thenReturn(type);

        CertificateCalculation cc = mock(CertificateCalculation.class);
        when(cc.getCalculation()).thenReturn(calculation);
        when(cc.getCertificate()).thenReturn(certificate);

        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of(cc));

        BigDecimal result = businessService.getCertificatePoints(ID);

        assertEquals(expectedResult, result);

        verify(persistenceService).getByCalculationId(ID);
    }

    @Test
    @DisplayName("Should return zero points if calculation has no certificates")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(ID)).thenReturn(List.of());

        BigDecimal result = businessService.getCertificatePoints(ID);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should validate and create certificate calculation")
    void shouldCreate() {
        CertificateCalculation entity = mock(CertificateCalculation.class);
        Certificate certificate = mock(Certificate.class);

        when(entity.getCertificate()).thenReturn(certificate);
        when(certificate.getId()).thenReturn(ID);
        when(persistenceService.getByCertificateId(ID)).thenReturn(List.of());
        when(persistenceService.save(entity)).thenReturn(entity);

        CertificateCalculation result = businessService.create(entity);

        assertEquals(entity, result);
        verify(validationService).validateOnCreate(entity);
        verify(validationService).validateDuplicateCertificateId(eq(entity), anyList());
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should validate and update certificate calculation")
    void shouldUpdate() {
        CertificateCalculation entity = mock(CertificateCalculation.class);

        when(persistenceService.getById(ID)).thenReturn(Optional.of(entity));
        when(persistenceService.save(entity)).thenReturn(entity);

        CertificateCalculation result = businessService.update(ID, entity);

        assertEquals(entity, result);
        verify(validationService).validateOnUpdate(ID, entity);
        verify(validationService).validateMemberForCalculation(entity);
        verify(entity).setId(ID);
        verify(persistenceService).save(entity);
    }

    @Test
    @DisplayName("Should throw PCTSException when updating non-existing entity")
    void shouldThrowWhenUpdatingNotFound() {
        CertificateCalculation entity = mock(CertificateCalculation.class);
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(ID, entity));

        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }

    @Test
    @DisplayName("Should create certificate calculations for calculation")
    void shouldCreateCertificateCalculations() {
        Calculation calculation = mock(Calculation.class);
        CertificateCalculation cc = mock(CertificateCalculation.class);
        Certificate certificate = mock(Certificate.class);

        when(cc.getCertificate()).thenReturn(certificate);
        when(certificate.getId()).thenReturn(ID);
        when(calculation.getCertificates()).thenReturn(List.of(cc));
        when(persistenceService.getByCertificateId(ID)).thenReturn(List.of());
        when(persistenceService.save(cc)).thenReturn(cc);

        List<CertificateCalculation> result = businessService.createCertificateCalculations(calculation);

        assertEquals(1, result.size());
        verify(cc).setCalculation(calculation);
        verify(validationService).validateOnCreate(cc);
    }

    @Test
    @DisplayName("Should update, create and delete certificate calculations correctly")
    void shouldUpdateCertificateCalculations() {
        Long certificateId = 10L;
        Long certificateCalculationId = 100L;

        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(ID);

        Certificate certificate = mock(Certificate.class);
        when(certificate.getId()).thenReturn(certificateId);

        CertificateCalculation existing = mock(CertificateCalculation.class);
        when(existing.getId()).thenReturn(certificateCalculationId);
        when(existing.getCalculation()).thenReturn(calculation);
        when(existing.getCertificate()).thenReturn(certificate);

        CertificateCalculation updated = mock(CertificateCalculation.class);
        when(updated.getCalculation()).thenReturn(calculation);
        when(updated.getCertificate()).thenReturn(certificate);

        when(calculation.getCertificates()).thenReturn(new ArrayList<>(List.of(updated)));

        when(persistenceService.getByCalculationId(ID)).thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.getById(certificateCalculationId)).thenReturn(Optional.of(existing));
        when(persistenceService.save(updated)).thenAnswer(inv -> inv.getArgument(0));

        List<CertificateCalculation> result = businessService.updateCertificateCalculations(calculation);

        assertEquals(1, result.size());
        verify(updated).setCalculation(calculation);
        verify(updated, times(2)).setId(certificateCalculationId);
        verify(validationService).validateOnUpdate(certificateCalculationId, updated);
    }
}
