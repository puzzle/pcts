package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CertificateTypeMapper.class)
class CertificateTypeMapperTest {
    @Autowired
    private CertificateTypeMapper mapper;

    @DisplayName("Should return certificate type")
    @Test
    void shouldReturnCertificateType() {
        CertificateTypeDto certificateTypeDto = new CertificateTypeDto(1L,
                                                                       "Bachelor of Business Administration",
                                                                       BigDecimal.ONE,
                                                                       "Test",
                                                                       List.of("Important tag", "One more tag"));

        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        CertificateType certificate = new CertificateType(1L,
                                                          "Bachelor of Business Administration",
                                                          BigDecimal.ONE,
                                                          "Test",
                                                          exampleTags);

        CertificateType result = mapper.fromDto(certificateTypeDto);
        assertEquals(certificate, result);
    }

    @DisplayName("Should return certificate type dto")
    @Test
    void shouldReturnCertificateTypeDto() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        CertificateType model = new CertificateType(1L, "Bachelor of Sciences", BigDecimal.ONE, "Test", exampleTags);
        CertificateTypeDto dto = new CertificateTypeDto(1L,
                                                        "Bachelor of Sciences",
                                                        BigDecimal.ONE,
                                                        "Test",
                                                        List.of("Important tag", "One more tag"));

        CertificateTypeDto result = mapper.toDto(model);
        assertEquals(dto, result);
    }

    @DisplayName("Should return list of certificate types")
    @Test
    void shouldGetListOfCertificateTypes() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        List<CertificateTypeDto> dtos = List
                .of(new CertificateTypeDto(1L,
                                           "Certificate in Advanced English",
                                           BigDecimal.ONE,
                                           "Test",
                                           List.of("Important tag", "One more tag")),
                    new CertificateTypeDto(2L,
                                           "Certificate of Advanced Studies",
                                           BigDecimal.ONE,
                                           "Test",
                                           List.of("Important tag", "One more tag")));
        List<CertificateType> models = List
                .of(new CertificateType(1L, "Certificate in Advanced English", BigDecimal.ONE, "Test", exampleTags),
                    new CertificateType(2L, "Certificate of Advanced Studies", BigDecimal.ONE, "Test", exampleTags));

        List<CertificateType> result = mapper.fromDto(dtos);
        assertEquals(models, result);
    }

    @DisplayName("Should return list of certificate type dtos")
    @Test
    void shouldGetListOfCertificateTypeDtos() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        List<CertificateType> models = List
                .of(new CertificateType(1L,
                                        "Executive Master of Business Administration",
                                        BigDecimal.ONE,
                                        "Test",
                                        exampleTags),
                    new CertificateType(2L, "Master of Science", BigDecimal.ONE, "Test", exampleTags));
        List<CertificateTypeDto> dtos = List
                .of(new CertificateTypeDto(1L,
                                           "Executive Master of Business Administration",
                                           BigDecimal.ONE,
                                           "Test",
                                           List.of("Important tag", "One more tag")),
                    new CertificateTypeDto(2L,
                                           "Master of Science",
                                           BigDecimal.ONE,
                                           "Test",
                                           List.of("Important tag", "One more tag")));

        List<CertificateTypeDto> result = mapper.toDto(models);
        assertEquals(dtos, result);
    }
}
