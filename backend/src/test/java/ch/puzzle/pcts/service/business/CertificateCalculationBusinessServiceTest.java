package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
class CertificateCalculationBusinessServiceTest
        extends
            BaseBusinessTest<CertificateCalculation, CertificateCalculationPersistenceService, CertificateCalculationValidationService, CertificateCalculationBusinessService> {

    private static final Long CERTIFICATE_CALCULATION_ID_1 = 1L;
    private static final Long CERTIFICATE_CALCULATION_ID_2 = 2L;
    private static final Long CERTIFICATE_ID = 1L;

    @Mock
    private CertificateCalculationValidationService validationService;

    @Mock
    private CertificateCalculationPersistenceService persistenceService;

    @InjectMocks
    private CertificateCalculationBusinessService businessService;

    @Override
    CertificateCalculation getModel() {
        return mock(CertificateCalculation.class);
    }

    @Override
    CertificateCalculationPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    CertificateCalculationValidationService getValidationService() {
        return validationService;
    }

    @Override
    CertificateCalculationBusinessService getBusinessService() {
        return businessService;
    }

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
        when(persistenceService.getByCalculationId(CERTIFICATE_CALCULATION_ID_1)).thenReturn(List.of(cc));

        List<CertificateCalculation> result = businessService.getByCalculationId(CERTIFICATE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByCalculationId(CERTIFICATE_CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should get certificate calculations by certificate id")
    void shouldGetByCertificateId() {
        CertificateCalculation cc = mock(CertificateCalculation.class);
        when(persistenceService.getByCertificateId(CERTIFICATE_CALCULATION_ID_1)).thenReturn(List.of(cc));

        List<CertificateCalculation> result = businessService.getByCertificateId(CERTIFICATE_CALCULATION_ID_1);

        assertEquals(1, result.size());
        verify(persistenceService).getByCertificateId(CERTIFICATE_CALCULATION_ID_1);
    }

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

        when(persistenceService.getByCalculationId(CERTIFICATE_CALCULATION_ID_1)).thenReturn(List.of(cc));

        BigDecimal result = businessService.getCertificatePoints(CERTIFICATE_CALCULATION_ID_1);

        assertEquals(0, expectedResult.compareTo(result));

        verify(persistenceService).getByCalculationId(CERTIFICATE_CALCULATION_ID_1);
    }

    @Test
    @DisplayName("Should return zero points if calculation has no certificates")
    void shouldReturnZeroPoints() {
        when(persistenceService.getByCalculationId(CERTIFICATE_CALCULATION_ID_1)).thenReturn(List.of());

        BigDecimal result = businessService.getCertificatePoints(CERTIFICATE_CALCULATION_ID_1);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Override
    @DisplayName("Should create certificate calculations for calculation")
    void shouldCreate() {
        Calculation calculation = mock(Calculation.class);
        CertificateCalculation cc = mock(CertificateCalculation.class);
        Certificate certificate = mock(Certificate.class);

        when(cc.getCertificate()).thenReturn(certificate);
        when(certificate.getId()).thenReturn(CERTIFICATE_CALCULATION_ID_1);
        when(calculation.getCertificates()).thenReturn(List.of(cc));
        when(persistenceService.getByCertificateId(CERTIFICATE_CALCULATION_ID_1)).thenReturn(List.of());
        when(persistenceService.save(cc)).thenReturn(cc);

        List<CertificateCalculation> result = businessService.createCertificateCalculations(calculation);

        assertEquals(1, result.size());
        verify(cc).setCalculation(calculation);
        verify(validationService).validateOnCreate(cc);
    }

    @Test
    @Override
    @DisplayName("Should update certificate calculations and delete removed ones")
    void shouldUpdate() {
        Calculation calculation = mock(Calculation.class);
        when(calculation.getId()).thenReturn(CERTIFICATE_CALCULATION_ID_1);

        Certificate certificate = mock(Certificate.class);
        when(certificate.getId()).thenReturn(CERTIFICATE_ID);

        CertificateCalculation existing = new CertificateCalculation();
        existing.setId(CERTIFICATE_CALCULATION_ID_2);

        CertificateCalculation updated = new CertificateCalculation();
        updated.setId(CERTIFICATE_CALCULATION_ID_2);
        updated.setCertificate(certificate);

        when(calculation.getCertificates()).thenReturn(List.of(updated));
        when(persistenceService.getByCalculationId(CERTIFICATE_CALCULATION_ID_1))
                .thenReturn(new ArrayList<>(List.of(existing)));
        when(persistenceService.save(any(CertificateCalculation.class))).thenAnswer(inv -> inv.getArgument(0));

        List<CertificateCalculation> result = businessService.updateCertificateCalculations(calculation);

        assertEquals(1, result.size());

        verify(validationService).validateOnUpdate(CERTIFICATE_CALCULATION_ID_2, updated);

        verify(persistenceService).delete(anyLong());
    }
}
