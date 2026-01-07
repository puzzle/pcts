package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
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

    private Certificate createCertificate(CertificateKind kind) {
        Certificate cert = new Certificate();
        cert.setId(CERT_ID);
        CertificateType type = new CertificateType();
        type.setCertificateKind(kind);
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
        Certificate certificate = createCertificate(CertificateKind.CERTIFICATE);
        CertificateCalculation model = createCertificateCalculation(certificate);

        CertificateDto mockedDto = mockCertificateDto();
        when(certificateMapper.toDto(certificate)).thenReturn(mockedDto);

        CertificateCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(ID, result.id());
        assertEquals(mockedDto, result.certificate());

        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should return null when certificate kind is not Certificate")
    @Test
    void shouldReturnNull() {
        Certificate certificate = createCertificate(CertificateKind.LEADERSHIP_TRAINING);
        CertificateCalculation model = createCertificateCalculation(certificate);

        CertificateCalculationDto result = mapper.toDto(model);

        assertNull(result);
        verify(certificateMapper, never()).toDto(any(Certificate.class));
    }

    @DisplayName("Should map List<CertificateCalculation> to List<CertificateCalculationDto> and remove LeadershipExperiences")
    @Test
    void shouldMapListToDto() {
        Certificate certificate = createCertificate(CertificateKind.CERTIFICATE);
        Certificate leadershipExperience = createCertificate(CertificateKind.LEADERSHIP_TRAINING);

        CertificateCalculation certificateCalculation = createCertificateCalculation(certificate);
        CertificateCalculation leadershipExperienceCalculation = createCertificateCalculation(leadershipExperience);

        CertificateDto mockedDto = mockCertificateDto();
        when(certificateMapper.toDto(certificate)).thenReturn(mockedDto);

        List<CertificateCalculationDto> result = mapper
                .toDto(List.of(certificateCalculation, leadershipExperienceCalculation));

        assertEquals(1, result.size());
        assertEquals(mockedDto, result.getFirst().certificate());
        verify(certificateMapper).toDto(certificate);
    }

    @DisplayName("Should map ID to CertificateCalculation")
    @Test
    void shouldMapFromId() {
        Certificate certificate = createCertificate(CertificateKind.CERTIFICATE);

        when(certificateBusinessService.getById(CERT_ID)).thenReturn(certificate);
        when(dto.id()).thenReturn(null);
        when(dto.certificateId()).thenReturn(CERT_ID);

        CertificateCalculation result = mapper.fromDto(dto);

        assertNotNull(result);
        assertEquals(certificate, result.getCertificate());
        assertNull(result.getId());

        verify(certificateBusinessService).getById(CERT_ID);
    }

    @DisplayName("Should map List<Long> to List<CertificateCalculation>")
    @Test
    void shouldMapListFromIds() {
        when(certificateBusinessService.getById(CERT_ID)).thenReturn(createCertificate(CertificateKind.CERTIFICATE));
        when(dto.id()).thenReturn(null);
        when(dto.certificateId()).thenReturn(CERT_ID);

        List<CertificateCalculation> result = mapper.fromDto(List.of(dto));

        assertEquals(1, result.size());
        verify(certificateBusinessService).getById(CERT_ID);
    }
}
