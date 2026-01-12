package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceCalculationMapperTest {

    private static final Long EXPERIENCE_ID = 10L;

    @Mock
    private LeadershipExperienceCalculationInputDto dto;

    @Mock
    private LeadershipExperienceBusinessService leadershipExperienceBusinessService;

    @Mock
    private LeadershipExperienceMapper leadershipExperienceMapper;

    @InjectMocks
    private LeadershipExperienceCalculationMapper mapper;

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

    @DisplayName("Should map List<CertificateCalculation> to List<LeadershipExperienceCalculationDto> and remove Certificates")
    @Test
    void shouldMapListToDto() {
        Certificate leadershipExperience = createLeadershipExperience();

        CertificateCalculation leadershipCalc = createCertificateCalculation(leadershipExperience);

        LeadershipExperienceDto mockedDto = mockLeadershipExperienceDto();
        when(leadershipExperienceMapper.toDto(leadershipExperience)).thenReturn(mockedDto);

        List<LeadershipExperienceCalculationDto> result = mapper.toDto(List.of(leadershipCalc));

        assertEquals(1, result.size());
        assertEquals(mockedDto, result.getFirst().experience());

        verify(leadershipExperienceMapper).toDto(leadershipExperience);

    }

    @DisplayName("Should map ID to CertificateCalculation")
    @Test
    void shouldMapFromId() {
        Certificate experience = createLeadershipExperience();
        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID)).thenReturn(experience);
        when(dto.id()).thenReturn(null);
        when(dto.leadershipExperienceId()).thenReturn(EXPERIENCE_ID);

        CertificateCalculation result = mapper.fromDto(dto);

        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(experience, result.getCertificate());

        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }

    @DisplayName("Should map List<Long> to List<CertificateCalculation>")
    @Test
    void shouldMapListFromIds() {
        when(dto.id()).thenReturn(null);
        when(dto.leadershipExperienceId()).thenReturn(EXPERIENCE_ID);
        when(leadershipExperienceBusinessService.getById(EXPERIENCE_ID))
                .thenReturn(createLeadershipExperience());

        List<CertificateCalculation> result = mapper.fromDto(List.of(dto));

        assertEquals(1, result.size());
        verify(leadershipExperienceBusinessService).getById(EXPERIENCE_ID);
    }
}
