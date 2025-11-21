package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {
    @InjectMocks
    private CertificateValidationService service;

    @Mock
    private CertificatePersistenceService persistenceService;

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
                .withValidUntil(LocalDate.now())
                .withComment("Comment")
                .build();
    }

    static Stream<Arguments> invalidModelProvider() {
        final Member validMember = createMember();
        final CertificateType validCertificateType = createCertificateType();
        final LocalDate validPastDate = LocalDate.of(1990, 1, 1);

        return Stream
                .of(Arguments
                        .of(createCertificate(null, validCertificateType, validPastDate),
                            "Certificate.member must not be null."),
                    Arguments
                            .of(createCertificate(validMember, null, validPastDate),
                                "Certificate.certificateType must not be null."),
                    Arguments
                            .of(createCertificate(validMember, validCertificateType, null),
                                "Certificate.completedAt must not be null."));
    }

    @DisplayName("Should throw exception on validateOnCreate() when competedAt is after validUntil")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameCompletedAtIsAfterValidUntil() {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(LocalDate.now().plusDays(1));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate));

        assertEquals("Certificate.completedAT must be before the validUntil date, given validUntil: "
                     + certificate.getValidUntil() + " and completedAt: " + certificate.getCompletedAt() + ".",
                     exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when competedAt is before validUntil")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameCompletedAtIsAfterValidUntil() {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(LocalDate.now().plusDays(1));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(1L, certificate));

        assertEquals("Certificate.completedAT must be before the validUntil date, given validUntil: "
                     + certificate.getValidUntil() + " and completedAt: " + certificate.getCompletedAt() + ".",
                     exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should pass validateOnCreate() when validUntil is null")
    @Test
    void shouldPassValidateOnCreateWhenValidUntilIsNull() {
        Certificate certificate = getValidModel();

        certificate.setValidUntil(null);

        assertDoesNotThrow(() -> service.validateOnCreate(certificate));
    }

    @DisplayName("Should pass validateOnUpdate() when validUntil is null")
    @Test
    void shouldPassValidateOnUpdateWhenValidUntilIsNull() {
        Certificate certificate = getValidModel();

        certificate.setValidUntil(null);

        assertDoesNotThrow(() -> service.validateOnUpdate(1L, certificate));
    }

    @DisplayName("Should pass validateOnCreate() when completedAt is exactly the same as validUntil")
    @Test
    void shouldPassValidateOnCreateWhenCompletedAtIsEqualToValidUntil() {
        Certificate certificate = getValidModel();

        certificate.setCompletedAt(LocalDate.now());

        assertDoesNotThrow(() -> service.validateOnCreate(certificate));
    }

    @DisplayName("Should pass validateOnUpdate() when completedAt is exactly the same as validUntil")
    @Test
    void shouldPassValidateOnUpdateWhenCompletedAtIsEqualToValidUntil() {
        Certificate certificate = getValidModel();

        certificate.setCompletedAt(LocalDate.now());

        assertDoesNotThrow(() -> service.validateOnUpdate(1L, certificate));
    }

    @DisplayName("Should not throw when completedAt is null")
    @Test
    void shouldNotThrowWhenCompletedAtIsNull() {
        LocalDate validUntil = LocalDate.now();

        assertDoesNotThrow(() -> service.validateCompletedAtIsBeforeValidUntil(null, validUntil));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Certificate certificate = getValidModel();

        CertificateValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(certificate);

        verify(spyService).validateOnCreate(certificate);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Certificate certificate = getValidModel();

        CertificateValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, certificate);

        verify(spyService).validateOnUpdate(id, certificate);
        verifyNoMoreInteractions(persistenceService);
    }
}