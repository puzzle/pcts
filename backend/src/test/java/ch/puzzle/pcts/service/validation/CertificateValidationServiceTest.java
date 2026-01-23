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
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {

    @Spy
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

    @DisplayName("Should pass validation when completedAt is before validUntil")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenBirthDateIsBeforeHireDate(String opName,
                                                 BiConsumer<CertificateValidationService, Certificate> validator) {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(LocalDate.now().minusYears(20));
        certificate.setValidUntil(LocalDate.now());

        assertDoesNotThrow(() -> validator.accept(service, certificate));
    }

    @DisplayName("Should pass validation when completedAt equals validUntil")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenCompletedAtEqualsValidUntil(String opName,
                                                   BiConsumer<CertificateValidationService, Certificate> validator) {
        Certificate certificate = getValidModel();
        LocalDate date = LocalDate.now();
        certificate.setCompletedAt(date);
        certificate.setValidUntil(date);

        assertDoesNotThrow(() -> validator.accept(service, certificate));
    }

    @DisplayName("Should pass validation when completedAt is null")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenCompletedAtIsNull(String opName,
                                         BiConsumer<CertificateValidationService, Certificate> validator) {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(null);

        assertDoesNotThrow(() -> validator.accept(service, certificate));
    }

    @DisplayName("Should pass validation when validUntil is null")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldPassWhenValidUntilIsNull(String opName,
                                        BiConsumer<CertificateValidationService, Certificate> validator) {
        Certificate certificate = getValidModel();
        certificate.setValidUntil(null);

        assertDoesNotThrow(() -> validator.accept(service, certificate));
    }

    @DisplayName("Should throw exception when completedAt is after validUntil")
    @ParameterizedTest(name = "{0}")
    @MethodSource("validationOperations")
    void shouldFailWhenCompletedAtIsAfterValidUntil(String opName,
                                                    BiConsumer<CertificateValidationService, Certificate> validator) {
        Certificate certificate = getValidModel();
        certificate.setCompletedAt(LocalDate.now().plusDays(1));
        certificate.setValidUntil(LocalDate.now());

        PCTSException exception = assertThrows(PCTSException.class, () -> validator.accept(service, certificate));

        assertEquals(List
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
                            certificate.getValidUntil().toString())),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate methods on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Certificate certificate = getValidModel();

        doNothing().when((ValidationBase<Certificate>) service).validateDateIsBefore(any(), any(), any(), any(), any());

        service.validateOnCreate(certificate);

        verify(service).validateOnCreate(certificate);
        verify(service)
                .validateDateIsBefore(CERTIFICATE,
                                      "completedAt",
                                      certificate.getCompletedAt(),
                                      "validUntil",
                                      certificate.getValidUntil());

        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate methods on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Certificate certificate = getValidModel();

        doNothing().when((ValidationBase<Certificate>) service).validateDateIsBefore(any(), any(), any(), any(), any());

        service.validateOnUpdate(CERTIFICATE_1_ID, certificate);

        verify(service).validateOnUpdate(CERTIFICATE_1_ID, certificate);
        verify(service)
                .validateDateIsBefore(CERTIFICATE,
                                      "completedAt",
                                      certificate.getCompletedAt(),
                                      "validUntil",
                                      certificate.getValidUntil());

        verifyNoMoreInteractions(persistenceService);
    }

    static Stream<Arguments> validationOperations() {
        return Stream
                .of(Arguments
                        .of("validateOnCreate",
                            (BiConsumer<CertificateValidationService, Certificate>) CertificateValidationService::validateOnCreate),

                    Arguments
                            .of("validateOnUpdate",
                                (BiConsumer<CertificateValidationService, Certificate>) (svc, cert) -> svc
                                        .validateOnUpdate(CERTIFICATE_1_ID, cert)));
    }
}