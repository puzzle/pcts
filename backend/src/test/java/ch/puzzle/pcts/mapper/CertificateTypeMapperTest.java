package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CertificateTypeMapper.class)
class CertificateTypeMapperTest {

    private final List<CertificateType> models = List.of(CERT_TYPE_5, CERT_TYPE_6);

    private final List<CertificateTypeDto> dtos = List.of(CERT_TYPE_5_DTO, CERT_TYPE_6_DTO);

    @Autowired
    private CertificateTypeMapper mapper;

    @DisplayName("Should return certificate type")
    @Test
    void shouldReturnCertificateType() {
        CertificateType result = mapper.fromDto(CERT_TYPE_5_DTO);
        assertEquals(CERT_TYPE_5, result);
    }

    @DisplayName("Should return certificate type dto")
    @Test
    void shouldReturnCertificateTypeDto() {
        CertificateTypeDto result = mapper.toDto(CERT_TYPE_5);
        assertEquals(CERT_TYPE_5_DTO, result);
    }

    @DisplayName("Should return list of certificate types")
    @Test
    void shouldGetListOfCertificateTypes() {
        List<CertificateType> result = mapper.fromDto(dtos);
        assertEquals(models, result);
    }

    @DisplayName("Should return list of certificate type dtos")
    @Test
    void shouldGetListOfCertificateTypeDtos() {
        List<CertificateTypeDto> result = mapper.toDto(models);
        assertEquals(dtos, result);
    }
}
