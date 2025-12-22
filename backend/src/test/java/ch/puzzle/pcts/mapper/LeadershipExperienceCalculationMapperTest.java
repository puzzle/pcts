package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceCalculationMapperTest {

    private static final Long EXPERIENCE_ID = 10L;

    @Mock
    private LeadershipExperienceBusinessService leadershipExperienceBusinessService;

    @Mock
    private LeadershipExperienceMapper leadershipExperienceMapper;

    private LeadershipExperienceCalculationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LeadershipExperienceCalculationMapper(leadershipExperienceBusinessService,
                                                           leadershipExperienceMapper);
    }

    private Certificate createCertificate(CertificateKind kind, Long id) {
        Certificate certificate = new Certificate();
        certificate.setId(id);

        CertificateType type = new CertificateType();
        type.setCertificateKind(kind);
        certificate.setCertificateType(type);

        return certificate;
    }

    private Certificate createLeadershipExperience() {
        return createCertificate(CertificateKind.LEADERSHIP_TRAINING, EXPERIENCE_ID);
    }

    private Certificate createCertificateOnly() {
        return createCertificate(CertificateKind.CERTIFICATE, null);
    }

    private CertificateCalculation createCertificateCalculation(Certificate certificate) {
        return new CertificateCalculation(5L, null, certificate);
    }

    private LeadershipExperienceDto mockLeadershipExperienceDto() {
        return mock(LeadershipExperienceDto.class);
    }

    @DisplayName("Should map CertificateCalculation to LeadershipExperienceCalculationDto")
    @Test
    void shouldMapToDto() {
        Certificate experience = createLeadershipExperience();
        CertificateCalculation model = createCertificateCalculation(experience);

        LeadershipExperienceDto mockedDto = mockLeadershipExperienceDto();
        when(leadershipExperienceMapper.toDto(experience)).thenReturn(mockedDto);

        LeadershipExperienceCalculationDto result = mapper.toDto(model);

        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals(mockedDto, result.experience());

        verify(leadershipExperienceMapper).toDto(experience);
    }

    @DisplayName("Should return null when certificate kind is CERTIFICATE")
    @Test
    void shouldReturnNullWhenCertificateKindIsCertificate() {
        CertificateCalculation model = createCertificateCalculation(createCertificateOnly());

        LeadershipExperienceCalculationDto result = mapper.toDto(model);

        assertNull(result);
        verify(leadershipExperienceMapper, never()).toDto(any(Certificate.class));
    }

    @DisplayName("Should map List<CertificateCalculation> to List<LeadershipExperienceCalculationDto> and remove Certificates")
    @Test
    void shouldMapListToDto() {
        Certificate leadershipExperience = createLeadershipExperience();
        Certificate certificate = createCertificateOnly();

        CertificateCalculation leadershipCalc = createCertificateCalculation(leadershipExperience);
        CertificateCalculation certificateCalc = createCertificateCalculation(certificate);

        LeadershipExperienceDto mockedDto = mockLeadershipExperienceDto();
        when(leadershipExperienceMapper.toDto(leadershipExperience)).thenReturn(mockedDto);

        List<LeadershipExperienceCalculationDto> result = mapper.toDto(List.of(leadershipCalc, certificateCalc));

        assertEquals(1, result.size());
        assertEquals(mockedDto, result.get(0).experience());

        verify(leadershipExperienceMapper).toDto(leadershipExperience);
        verify(leadershipExperienceMapper, never()).toDto(certificate);
    }

    @DisplayName("Should map ID to CertificateCalculation")
    @Test
    void shouldMapFromId() {
        Certificate experience = createLeadershipExperience();
        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID)).thenReturn(experience);

        CertificateCalculation result = mapper.fromDto(EXPERIENCE_ID);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(experience, result.getCertificate());

        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should map List<Long> to List<CertificateCalculation>")
    @Test
    void shouldMapListFromIds() {
        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID))
                .thenReturn(createLeadershipExperience());

        List<CertificateCalculation> result = mapper.fromDto(List.of(EXPERIENCE_ID));

        assertEquals(1, result.size());
        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should throw when leadership experience not found")
    @Test
    void shouldThrowWhenLeadershipExperienceNotFound() {
        when(leadershipExperienceBusinessService.getById(anyLong()))
                .thenThrow(new RuntimeException("Leadership experience not found"));

        assertThrows(RuntimeException.class, () -> mapper.fromDto(EXPERIENCE_ID));
    }
}
