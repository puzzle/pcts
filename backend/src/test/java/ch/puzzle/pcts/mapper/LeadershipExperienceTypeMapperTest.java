package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LeadershipExperienceTypeMapper.class)
class LeadershipExperienceTypeMapperTest {
    @Autowired
    private LeadershipExperienceTypeMapper mapper;

    @DisplayName("Should return certificate type")
    @Test
    void shouldReturnCertificate() {
        LeadershipExperienceTypeDto leadershipExperienceTypeDto = new LeadershipExperienceTypeDto(1L,
                                                                                                  "New LeadershipExperience",
                                                                                                  BigDecimal.ONE,
                                                                                                  "This is very important",
                                                                                                  CertificateKind.LEADERSHIP_TRAINING);

        CertificateType certificate = new CertificateType(1L,
                                                          "New LeadershipExperience",
                                                          BigDecimal.ONE,
                                                          "This is very important",
                                                          CertificateKind.LEADERSHIP_TRAINING);

        CertificateType result = mapper.fromDto(leadershipExperienceTypeDto);
        assertEquals(certificate, result);
    }

    @DisplayName("Should return leadershipExperienceTypeDto")
    @Test
    void shouldReturnCertificateDto() {
        CertificateType model = new CertificateType(1L,
                                                    "Chancellor of Jupiter",
                                                    BigDecimal.ONE,
                                                    "Very difficult",
                                                    CertificateKind.LEADERSHIP_TRAINING);

        LeadershipExperienceTypeDto dto = new LeadershipExperienceTypeDto(1L,
                                                                          "Chancellor of Jupiter",
                                                                          BigDecimal.ONE,
                                                                          "Very difficult",
                                                                          CertificateKind.LEADERSHIP_TRAINING);

        LeadershipExperienceTypeDto result = mapper.toDto(model);
        assertEquals(dto, result);
    }

    @DisplayName("Should return list of certificate type")
    @Test
    void shouldReturnListOfCertificates() {

        List<CertificateType> certificates = List
                .of(new CertificateType(1L,
                                        "Chancellor of Jupiter",
                                        BigDecimal.ONE,
                                        "Very difficult",
                                        CertificateKind.YOUTH_AND_SPORT),
                    new CertificateType(2L, "Sergeant", BigDecimal.TWO, "", CertificateKind.MILITARY_FUNCTION));

        List<LeadershipExperienceTypeDto> dtos = List
                .of(new LeadershipExperienceTypeDto(1L,
                                                    "Chancellor of Jupiter",
                                                    BigDecimal.ONE,
                                                    "Very difficult",
                                                    CertificateKind.YOUTH_AND_SPORT),
                    new LeadershipExperienceTypeDto(2L,
                                                    "Sergeant",
                                                    BigDecimal.TWO,
                                                    "",
                                                    CertificateKind.MILITARY_FUNCTION));

        List<CertificateType> result = mapper.fromDto(dtos);
        assertEquals(certificates, result);
    }

    @DisplayName("Should return list of leadershipExperienceTypeDtos")
    @Test
    void shouldReturnListOfLeadershipExperienceDtos() {

        List<CertificateType> certificates = List
                .of(new CertificateType(1L,
                                        "Chancellor of Jupiter",
                                        BigDecimal.ONE,
                                        "Very difficult",
                                        CertificateKind.YOUTH_AND_SPORT),
                    new CertificateType(2L, "Sergeant", BigDecimal.TWO, "", CertificateKind.MILITARY_FUNCTION));

        List<LeadershipExperienceTypeDto> dtos = List
                .of(new LeadershipExperienceTypeDto(1L,
                                                    "Chancellor of Jupiter",
                                                    BigDecimal.ONE,
                                                    "Very difficult",
                                                    CertificateKind.YOUTH_AND_SPORT),
                    new LeadershipExperienceTypeDto(2L,
                                                    "Sergeant",
                                                    BigDecimal.TWO,
                                                    "",
                                                    CertificateKind.MILITARY_FUNCTION));

        List<LeadershipExperienceTypeDto> result = mapper.toDto(certificates);
        assertEquals(dtos, result);
    }
}
