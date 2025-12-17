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

    private Certificate createLeadershipExperience() {
        Certificate experience = new Certificate();
        experience.setId(EXPERIENCE_ID);

        CertificateType type = new CertificateType();
        type.setCertificateKind(CertificateKind.LEADERSHIP_TRAINING);
        experience.setCertificateType(type);

        return experience;
    }

    private CertificateCalculation createModel(Certificate certificate) {
        return new CertificateCalculation(5L, null, certificate);
    }

    @DisplayName("Should map CertificateCalculation to LeadershipExperienceCalculationDto")
    @Test
    void shouldMapToDto() {
        Certificate experience = createLeadershipExperience();
        CertificateCalculation model = createModel(experience);

        LeadershipExperienceDto mockedDto = mock(LeadershipExperienceDto.class);
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
        Certificate certificate = new Certificate();
        CertificateType type = new CertificateType();
        type.setCertificateKind(CertificateKind.CERTIFICATE);
        certificate.setCertificateType(type);

        CertificateCalculation model = createModel(certificate);

        LeadershipExperienceCalculationDto result = mapper.toDto(model);

        assertNull(result);
        verify(leadershipExperienceMapper, never()).toDto(any(Certificate.class));
    }

    @DisplayName("Should map List<CertificateCalculation> to List<LeadershipExperienceCalculationDto> and remove Certificates")
    @Test
    void shouldMapListToDto() {
        Certificate leadershipExperience = createLeadershipExperience();

        Certificate certificate = new Certificate();
        CertificateType certificateType = new CertificateType();
        certificateType.setCertificateKind(CertificateKind.CERTIFICATE);
        certificate.setCertificateType(certificateType);

        CertificateCalculation leadershipExperienceCalculation = createModel(leadershipExperience);
        CertificateCalculation certificateCalculation = createModel(certificate);

        LeadershipExperienceDto mockedDto = mock(LeadershipExperienceDto.class);
        when(leadershipExperienceMapper.toDto(leadershipExperience)).thenReturn(mockedDto);

        List<LeadershipExperienceCalculationDto> result = mapper
                .toDto(List.of(leadershipExperienceCalculation, certificateCalculation));

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
