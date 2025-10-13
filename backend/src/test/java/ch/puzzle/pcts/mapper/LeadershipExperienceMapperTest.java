package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.leadership_experience.LeadershipExperienceDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(classes = LeadershipExperienceMapper.class)
class LeadershipExperienceMapperTest {
    @Autowired
    private LeadershipExperienceMapper mapper;

    @DisplayName("Should return certificate")
    @Test
    void shouldReturnCertificate() {
        LeadershipExperienceDto leadershipExperienceDto = new LeadershipExperienceDto(1L,
                                                                                      "New LeadershipExperience",
                                                                                      BigDecimal.ONE,
                                                                                      "This is very important",
                                                                                      CertificateType.LEADERSHIP_TRAINING);

        Certificate certificate = new Certificate(1L,
                                                  "New LeadershipExperience",
                                                  BigDecimal.ONE,
                                                  "This is very important",
                                                  CertificateType.LEADERSHIP_TRAINING);

        Certificate result = mapper.fromDto(leadershipExperienceDto);
        assertEquals(certificate.toString(), result.toString());
    }

    @DisplayName("Should return leadershipExperienceDto")
    @Test
    void shouldReturnCertificateDto() {
        Certificate model = new Certificate(1L,
                                            "Chancellor of Jupiter",
                                            BigDecimal.ONE,
                                            "Very difficult",
                                            CertificateType.LEADERSHIP_TRAINING);

        LeadershipExperienceDto dto = new LeadershipExperienceDto(1L,
                                                                  "Chancellor of Jupiter",
                                                                  BigDecimal.ONE,
                                                                  "Very difficult",
                                                                  CertificateType.LEADERSHIP_TRAINING);

        LeadershipExperienceDto result = mapper.toDto(model);
        assertEquals(dto.toString(), result.toString());
    }

    @DisplayName("Should return list of certificates")
    @Test
    void shouldReturnListOfCertificates() {

        List<Certificate> certificates = List
                .of(new Certificate(1L,
                                    "Chancellor of Jupiter",
                                    BigDecimal.ONE,
                                    "Very difficult",
                                    CertificateType.YOUTH_AND_SPORT),
                    new Certificate(2L, "Sergeant", BigDecimal.TWO, "", CertificateType.MILITARY_FUNCTION));

        List<LeadershipExperienceDto> dtos = List
                .of(new LeadershipExperienceDto(1L,
                                                "Chancellor of Jupiter",
                                                BigDecimal.ONE,
                                                "Very difficult",
                                                CertificateType.YOUTH_AND_SPORT),
                    new LeadershipExperienceDto(2L, "Sergeant", BigDecimal.TWO, "", CertificateType.MILITARY_FUNCTION));

        List<Certificate> result = mapper.fromDto(dtos);
        assertEquals(certificates.toString(), result.toString());
    }

    @DisplayName("Should return list of leadershipExperienceDtos")
    @Test
    void shouldReturnListOfLeadershipExperienceDtos() {

        List<Certificate> certificates = List
                .of(new Certificate(1L,
                                    "Chancellor of Jupiter",
                                    BigDecimal.ONE,
                                    "Very difficult",
                                    CertificateType.YOUTH_AND_SPORT),
                    new Certificate(2L, "Sergeant", BigDecimal.TWO, "", CertificateType.MILITARY_FUNCTION));

        List<LeadershipExperienceDto> dtos = List
                .of(new LeadershipExperienceDto(1L,
                                                "Chancellor of Jupiter",
                                                BigDecimal.ONE,
                                                "Very difficult",
                                                CertificateType.YOUTH_AND_SPORT),
                    new LeadershipExperienceDto(2L, "Sergeant", BigDecimal.TWO, "", CertificateType.MILITARY_FUNCTION));

        List<LeadershipExperienceDto> result = mapper.toDto(certificates);
        assertEquals(dtos.toString(), result.toString());
    }
}
