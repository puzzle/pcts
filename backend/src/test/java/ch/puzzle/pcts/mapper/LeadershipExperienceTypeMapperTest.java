package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
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
        CertificateType result = mapper.fromDto(LEADERSHIP_TYPE_4_DTO);
        assertEquals(LEADERSHIP_TYPE_4, result);
    }

    @DisplayName("Should return leadershipExperienceTypeDto")
    @Test
    void shouldReturnCertificateDto() {
        LeadershipExperienceTypeDto result = mapper.toDto(LEADERSHIP_TYPE_1);
        assertEquals(LEADERSHIP_TYPE_1_DTO, result);
    }

    @DisplayName("Should return list of certificate type")
    @Test
    void shouldReturnListOfCertificates() {
        List<CertificateType> certificates = List.of(LEADERSHIP_TYPE_4, LEADERSHIP_TYPE_5);

        List<LeadershipExperienceTypeDto> dtos = List.of(LEADERSHIP_TYPE_4_DTO, LEADERSHIP_TYPE_5_DTO);

        List<CertificateType> result = mapper.fromDto(dtos);
        assertEquals(certificates, result);
    }

    @DisplayName("Should return list of leadershipExperienceTypeDtos")
    @Test
    void shouldReturnListOfLeadershipExperienceDtos() {
        List<CertificateType> certificates = List.of(LEADERSHIP_TYPE_4, LEADERSHIP_TYPE_5);

        List<LeadershipExperienceTypeDto> dtos = List.of(LEADERSHIP_TYPE_4_DTO, LEADERSHIP_TYPE_5_DTO);

        List<LeadershipExperienceTypeDto> result = mapper.toDto(certificates);
        assertEquals(dtos, result);
    }
}
