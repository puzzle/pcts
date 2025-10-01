package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CertificateMapperTest {

    private CertificateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CertificateMapper();
    }

    @DisplayName("Should return certificate")
    @Test
    void shouldReturnCertificate() {
        CertificateDto certificateDto = new CertificateDto(1L,
                                                           "Bachelor of Business Administration",
                                                           BigDecimal.ONE,
                                                           "Test",
                                                           List.of("Important tag", "One more tag"));

        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        Certificate certificate = new Certificate(1L,
                                                  "Bachelor of Business Administration",
                                                  BigDecimal.ONE,
                                                  "Test",
                                                  exampleTags);

        Certificate result = mapper.fromDto(certificateDto);
        assertEquals(certificate.toString(), result.toString());
    }

    @DisplayName("Should return certificate dto")
    @Test
    void shouldReturnCertificateDto() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        Certificate model = new Certificate(1L, "Bachelor of Sciences", BigDecimal.ONE, "Test", exampleTags);
        CertificateDto dto = new CertificateDto(1L,
                                                "Bachelor of Sciences",
                                                BigDecimal.ONE,
                                                "Test",
                                                List.of("Important tag", "One more tag"));

        CertificateDto result = mapper.toDto(model);
        assertEquals(dto.toString(), result.toString());
    }

    @DisplayName("Should return list of certificates")
    @Test
    void shouldGetListOfCertificates() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        List<CertificateDto> dtos = List
                .of(new CertificateDto(1L,
                                       "Certificate in Advanced English",
                                       BigDecimal.ONE,
                                       "Test",
                                       List.of("Important tag", "One more tag")),
                    new CertificateDto(2L,
                                       "Certificate of Advanced Studies",
                                       BigDecimal.ONE,
                                       "Test",
                                       List.of("Important tag", "One more tag")));
        List<Certificate> models = List
                .of(new Certificate(1L, "Certificate in Advanced English", BigDecimal.ONE, "Test", exampleTags),
                    new Certificate(2L, "Certificate of Advanced Studies", BigDecimal.ONE, "Test", exampleTags));

        List<Certificate> result = mapper.fromDto(dtos);
        assertEquals(models.toString(), result.toString());
    }

    @DisplayName("Should return list of certificate dtos")
    @Test
    void shouldGetListOfCertificateDtos() {
        Set<Tag> exampleTags = new LinkedHashSet<>(List
                .of(new Tag(null, "Important tag"), new Tag(null, "One more tag")));

        List<Certificate> models = List
                .of(new Certificate(1L,
                                    "Executive Master of Business Administration",
                                    BigDecimal.ONE,
                                    "Test",
                                    exampleTags),
                    new Certificate(2L, "Master of Science", BigDecimal.ONE, "Test", exampleTags));
        List<CertificateDto> dtos = List
                .of(new CertificateDto(1L,
                                       "Executive Master of Business Administration",
                                       BigDecimal.ONE,
                                       "Test",
                                       List.of("Important tag", "One more tag")),
                    new CertificateDto(2L,
                                       "Master of Science",
                                       BigDecimal.ONE,
                                       "Test",
                                       List.of("Important tag", "One more tag")));

        List<CertificateDto> result = mapper.toDto(models);
        assertEquals(dtos.toString(), result.toString());
    }
}
