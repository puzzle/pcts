package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateCalculationMapperTest {

    private static final Long ID = 5L;
    private static final Long CERT_ID = 10L;

    @Mock
    private CertificateBusinessService certificateBusinessService;

    @Mock
    private CertificateCalculationInputDto dto;

    @Mock
    private CertificateMapper certificateMapper;

    @InjectMocks
    private CertificateCalculationMapper mapper;

    private Certificate createCertificate() {
        Certificate cert = new Certificate();
        cert.setId(CERT_ID);
        CertificateType type = new CertificateType();
        cert.setCertificateType(type);
        return cert;
    }

    private CertificateCalculation createCertificateCalculation(Certificate certificate) {
        return new CertificateCalculation(ID, null, certificate);
    }

    private CertificateDto mockCertificateDto() {
        return mock(CertificateDto.class);
    }

    @DisplayName("Should map CertificateCalculation to CertificateCalculationDto")
    @Test
    void shouldMapToDto() {
        Certificate certificate = createCertificate();
        CertificateCalculation model = createCertificateCalculation(certificate);

        CertificateDto mockedDto = mockCertificateDto();
        when(certificateMapper.toDto(certificate)).thenReturn(mockedDto);

        CertificateCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(ID, result.id());
        assertEquals(mockedDto, result.certificate());

        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should map List<CertificateCalculation> to List<CertificateCalculationDto>")
    @Test
    void shouldMapListToDto() {
        Certificate certificate = createCertificate();
        CertificateCalculation certificateCalculation = createCertificateCalculation(certificate);

        CertificateDto mockedDto = mockCertificateDto();
        when(certificateMapper.toDto(certificate)).thenReturn(mockedDto);

        List<CertificateCalculationDto> result = mapper.toDto(List.of(certificateCalculation));

        assertEquals(1, result.size());
        assertEquals(mockedDto, result.getFirst().certificate());

        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should map CertificateCalculationInputDto to CertificateCalculation")
    @Test
    void shouldMapFromDto() {
        Certificate certificate = createCertificate();

        when(certificateBusinessService.getById(CERT_ID)).thenReturn(certificate);
        when(dto.id()).thenReturn(null);
        when(dto.certificateId()).thenReturn(CERT_ID);

        CertificateCalculation result = mapper.fromDto(dto);

        assertNotNull(result);
        assertEquals(certificate, result.getCertificate());
        assertNull(result.getId());

        verify(certificateBusinessService).getById(CERT_ID);
    }

    @DisplayName("Should map List<CertificateCalculationInputDto> to List<CertificateCalculation>")
    @Test
    void shouldMapListFromDto() {
        when(certificateBusinessService.getById(CERT_ID)).thenReturn(createCertificate());
        when(dto.id()).thenReturn(null);
        when(dto.certificateId()).thenReturn(CERT_ID);

        List<CertificateCalculation> result = mapper.fromDto(List.of(dto));

        assertEquals(1, result.size());
        verify(certificateBusinessService).getById(CERT_ID);
    }
}