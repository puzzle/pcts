package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class CertificateMapperIT {
    @InjectMocks
    private CertificateMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should return certificate")
    @Test
    void shouldReturnCertificate() {
        CertificateDto certificateDto = new CertificateDto(1L,
                                                           "Bachelor of Business Administration",
                                                           BigDecimal.ONE,
                                                           "Test");
        Certificate certificate = new Certificate(1L, "Bachelor of Business Administration", BigDecimal.ONE, "Test");

        Certificate result = mapper.fromDto(certificateDto);
        assertEquals(certificate.toString(), result.toString());
    }

    @DisplayName("Should return certificate dto")
    @Test
    void shouldReturnCertificateDto() {
        Certificate model = new Certificate(1L, "Bachelor of Sciences", BigDecimal.ONE, "Test");
        CertificateDto dto = new CertificateDto(1L, "Bachelor of Sciences", BigDecimal.ONE, "Test");

        CertificateDto result = mapper.toDto(model);
        assertEquals(dto.toString(), result.toString());
    }

    @DisplayName("Should return list of certificates")
    @Test
    void shouldGetListOfCertificates() {
        List<CertificateDto> dtos = List
                .of(new CertificateDto(1L, "Certificate in Advanced English", BigDecimal.ONE, "Test"),
                    new CertificateDto(2L, "Certificate of Advanced Studies", BigDecimal.ONE, "Test"));
        List<Certificate> models = List
                .of(new Certificate(1L, "Certificate in Advanced English", BigDecimal.ONE, "Test"),
                    new Certificate(2L, "Certificate of Advanced Studies", BigDecimal.ONE, "Test"));

        List<Certificate> result = mapper.fromDto(dtos);
        assertEquals(models.toString(), result.toString());
    }

    @DisplayName("Should return list of certificate dtos")
    @Test
    void shouldGetListOfCertificateDtos() {
        List<Certificate> models = List
                .of(new Certificate(1L, "Executive Master of Business Administration", BigDecimal.ONE, "Test"),
                    new Certificate(2L, "Master of Science", BigDecimal.ONE, "Test"));
        List<CertificateDto> dtos = List
                .of(new CertificateDto(1L, "Executive Master of Business Administration", BigDecimal.ONE, "Test"),
                    new CertificateDto(2L, "Master of Science", BigDecimal.ONE, "Test"));

        List<CertificateDto> result = mapper.toDto(models);
        assertEquals(dtos.toString(), result.toString());
    }
}
