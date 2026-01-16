package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        m.setFirstName("Member");
        m.setLastName("Test");
        m.setAbbreviation("MT");
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(ORG_UNIT_2);
        return m;
    }

    private static CertificateType createCertificateType() {
        return CertificateType.Builder
                .builder()
                .withName("Certificate")
                .withPoints(BigDecimal.valueOf(10))
                .withComment("Comment")
                .withTags(Set.of(TAG_3))
                .withCertificateKind(CertificateKind.CERTIFICATE)
                .build();
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
        return Stream
                .of(Arguments
                        .of(createCertificate(null, CERT_TYPE_1, DATE_YESTERDAY),
                            List.of(Map.of(FieldKey.CLASS, "Certificate", FieldKey.FIELD, "member"))),

                    Arguments
                            .of(createCertificate(MEMBER_1, null, DATE_YESTERDAY),
                                List.of(Map.of(FieldKey.CLASS, "Certificate", FieldKey.FIELD, "certificateType"))));

    }

    @DisplayName("Should throw exception on validateOnCreate() and validateOnUpdate() when competedAt is after validUntil")
    @Test
    void shouldThrowExceptionOnValidateOnCreateAndValidateOnUpdateWhenCompletedAtIsAfterValidUntil() {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(LocalDate.now().plusDays(1));

        List<PCTSException> exceptions = List
                .of(assertThrows(PCTSException.class, () -> service.validateOnUpdate(CERTIFICATE_1_ID, certificate)),
                    assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate)));

        exceptions
                .forEach(exception -> assertEquals(List
                        .of(Map
                                .of(FieldKey.ENTITY,
                                    CERTIFICATE,
                                    FieldKey.FIELD,
                                    "completedAt",
                                    FieldKey.IS,
                                    certificate.getCompletedAt().toString(),
                                    FieldKey.CONDITION_FIELD,
                                    "validUntil",
                                    FieldKey.MAX,
                                    certificate.getValidUntil().toString())), exception.getErrorAttributes()));
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

        assertDoesNotThrow(() -> service.validateOnUpdate(CERTIFICATE_1_ID, certificate));
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

        assertDoesNotThrow(() -> service.validateOnUpdate(CERTIFICATE_1_ID, certificate));
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
        Certificate certificate = getValidModel();

        CertificateValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Certificate>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(CERTIFICATE_1_ID, certificate);

        verify(spyService).validateOnUpdate(CERTIFICATE_1_ID, certificate);
        verifyNoMoreInteractions(persistenceService);
    }
}