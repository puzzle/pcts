package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {

    @InjectMocks
    CertificateValidationService service;

    @Override
    Certificate getModel() {
        return new Certificate(null, "Certificate", BigDecimal.valueOf(10), "Comment", Set.of(new Tag(null, "Tag")));
    }

    @Override
    void validate() {

    }

    @Override
    CertificateValidationService getService() {
        return service;
    }
}
