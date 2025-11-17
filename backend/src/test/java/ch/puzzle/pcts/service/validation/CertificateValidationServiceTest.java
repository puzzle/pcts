package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.mode.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CertificateValidationServiceTest
        extends
            ValidationBaseServiceTest<Certificate, CertificateValidationService> {
    @InjectMocks
    private CertificateValidationService service;

    @Override
    Certificate getValidModel() {
        return createCertificate(createMember(), createCertificateType(), LocalDate.now().minusDays(1));
    }

    @Override
    CertificateValidationService getService() {
        return service;
    }

    private static Member createMember() {
        Member m = new Member();
        m.setEmploymentState(EmploymentState.MEMBER);
        m.setBirthDate(LocalDate.EPOCH);
        m.setName("Member");
        m.setLastName("Test");
        m.setAbbreviation("MT");
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    private static CertificateType createCertificateType() {
        return new CertificateType(null,
                                   "Certificate",
                                   BigDecimal.valueOf(10),
                                   "Comment",
                                   Set.of(new Tag(null, "Tag")),
                                   CertificateKind.CERTIFICATE);
    }

    private static Certificate createCertificate(Member member, CertificateType certificateType,
                                                 LocalDate completedAt) {
        return Certificate.Builder
                .builder()
                .withMember(member)
                .withCertificateType(certificateType)
                .withCompletedAt(completedAt)
                .withValidUntil(LocalDate.EPOCH)
                .withComment("Comment")
                .build();
    }

    static Stream<Arguments> invalidModelProvider() {
        final Member validMember = createMember();
        final CertificateType validCertificateType = createCertificateType();
        final LocalDate validPastDate = LocalDate.of(1990, 1, 1);
        final LocalDate futureDate = LocalDate.now().plusDays(1);

        return Stream
                .of(Arguments
                        .of(createCertificate(null, validCertificateType, validPastDate),
                            "Certificate.member must not be null."),
                    Arguments
                            .of(createCertificate(validMember, null, validPastDate),
                                "Certificate.certificateType must not be null."),
                    Arguments
                            .of(createCertificate(validMember, validCertificateType, null),
                                "Certificate.completedAt must not be null."),
                    Arguments
                            .of(createCertificate(validMember, validCertificateType, futureDate),
                                "Certificate.completedAt must be in the past, given " + futureDate + "."));
    }
}