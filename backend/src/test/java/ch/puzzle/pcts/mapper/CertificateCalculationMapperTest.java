package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateCalculationMapperTest {

    private static final Long CERT_ID = 10L;

    @Mock
    private CertificateBusinessService certificateBusinessService;

    @Mock
    private CertificateMapper certificateMapper;

    private CertificateCalculationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CertificateCalculationMapper(certificateBusinessService, certificateMapper);
    }

    private Certificate createCertificate() {
        Certificate cert = new Certificate();
        cert.setId(CERT_ID);
        return cert;
    }

    private CertificateCalculation createModel(Certificate cert) {
        return new CertificateCalculation(5L, null, cert);
    }

    @DisplayName("Should map CertificateCalculation to CertificateCalculationDto")
    @Test
    void shouldMapToDto() {
        Certificate certificate = createCertificate();
        CertificateCalculation model = createModel(certificate);

        CertificateDto mockedCertificateDto = mock(CertificateDto.class);
        when(certificateMapper.toDto(certificate)).thenReturn(mockedCertificateDto);

        CertificateCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals(mockedCertificateDto, result.certificate());

        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should map List<CertificateCalculation> to List<CertificateCalculationDto>")
    @Test
    void shouldMapListToDto() {
        Certificate certificate = createCertificate();
        CertificateCalculation model = createModel(certificate);

        CertificateDto mockedCertificateDto = mock(CertificateDto.class);
        when(certificateMapper.toDto(certificate)).thenReturn(mockedCertificateDto);

        List<CertificateCalculationDto> result = mapper.toDto(List.of(model));

        assertEquals(1, result.size());
        assertEquals(mockedCertificateDto, result.get(0).certificate());
        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should map ID to CertificateCalculation")
    @Test
    void shouldMapFromId() {
        Certificate certificate = createCertificate();

        when(certificateBusinessService.getById(CERT_ID)).thenReturn(certificate);

        CertificateCalculation result = mapper.fromDto(CERT_ID);

        assertNotNull(result);
        assertEquals(certificate, result.getCertificate());
        assertNull(result.getId());

        verify(certificateBusinessService).getById(CERT_ID);
    }

    @DisplayName("Should map List<Long> to List<CertificateCalculation>")
    @Test
    void shouldMapListFromIds() {
        when(certificateBusinessService.getById(CERT_ID)).thenReturn(createCertificate());

        List<CertificateCalculation> result = mapper.fromDto(List.of(CERT_ID));

        assertEquals(1, result.size());
        verify(certificateBusinessService).getById(CERT_ID);
    }

    @DisplayName("Should throw when certificate not found")
    @Test
    void shouldThrowWhenCertificateNotFound() {
        when(certificateBusinessService.getById(anyLong()))
                .thenThrow(new RuntimeException("Certificate not found"));

        assertThrows(RuntimeException.class, () -> mapper.fromDto(CERT_ID));
    }
}
