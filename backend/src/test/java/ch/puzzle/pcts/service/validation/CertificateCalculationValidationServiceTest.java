package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

class CertificateCalculationValidationServiceTest
        extends
            ValidationBaseServiceTest<CertificateCalculation, CertificateCalculationValidationService> {

    private static final Long MEMBER_ID_1 = 1L;
    private static final Long MEMBER_ID_2 = 2L;
    private static final Long CALCULATION_ID = 1L;
    private static final Long CERTIFICATE_CALCULATION_ID = 1L;
    private static final Long CERTIFICATE_ID = 1L;
    private static final Long ORGANISATION_UNIT_ID = 1L;

    private static final String FIRST_NAME_1 = "Alice";
    private static final String LAST_NAME_1 = "Smith";
    private static final String FIRST_NAME_2 = "Bob";
    private static final String LAST_NAME_2 = "Johnson";
    private static final String CERTIFICATE_NAME = "Certificate A";
    private static final String ORGANISATION_UNIT_NAME = "Organisation Unit";

    @Override
    CertificateCalculationValidationService getService() {
        return new CertificateCalculationValidationService();
    }

    @Override
    CertificateCalculation getValidModel() {
        Member member = createMember(MEMBER_ID_1, FIRST_NAME_1, LAST_NAME_1);
        Calculation calculation = createCalculation(CALCULATION_ID, member);
        Certificate certificate = createCertificate(CERTIFICATE_ID, member, CERTIFICATE_NAME);

        return new CertificateCalculation(null, calculation, certificate);
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(new CertificateCalculation(null, null, new Certificate()),
                            List.of(Map.of(FieldKey.CLASS, "CertificateCalculation", FieldKey.FIELD, "calculation"))),
                    Arguments
                            .of(new CertificateCalculation(null, new Calculation(), null),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateCalculation",
                                                    FieldKey.FIELD,
                                                    "certificate"))));
    }

    @DisplayName("Should throw exception when members do not match")
    @Test
    void shouldThrowExceptionWhenMembersDoNotMatch() {
        CertificateCalculationValidationService spyService = spy(getService());
        Member member2 = createMember(MEMBER_ID_2, FIRST_NAME_2, LAST_NAME_2);

        CertificateCalculation cc = getValidModel();
        cc.getCalculation().setMember(member2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(cc));

        assertEquals(ErrorKey.ATTRIBUTE_DOES_NOT_MATCH, exception.getErrorKeys().getFirst());
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "certificate", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should throw exception on duplicate certificate ID")
    @Test
    void shouldThrowExceptionOnDuplicateCertificateId() {
        CertificateCalculationValidationService spyService = spy(getService());
        CertificateCalculation cc = getValidModel();
        CertificateCalculation existingCc = getValidModel();
        existingCc.setId(CERTIFICATE_CALCULATION_ID);

        List<CertificateCalculation> existing = List.of(existingCc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateCertificateId(cc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().getFirst());
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "certificate", FieldKey.IS, CERTIFICATE_NAME),
                     exception.getErrorAttributes().getFirst());
    }

    @DisplayName("Should not throw exception when only same entity exists")
    @Test
    void shouldNotThrowWhenOnlySameEntityExists() {
        CertificateCalculationValidationService spyService = spy(getService());
        CertificateCalculation cc = getValidModel();
        cc.setId(CALCULATION_ID);

        List<CertificateCalculation> existing = List.of(cc);

        assertDoesNotThrow(() -> spyService.validateDuplicateCertificateId(cc, existing));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        CertificateCalculationValidationService spyService = spy(getService());
        CertificateCalculation cc = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(cc);

        spyService.validateOnCreate(cc);

        verify(spyService).validateMemberForCalculation(cc);
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member member = new Member();
        member.setId(id);
        member.setEmploymentState(EmploymentState.APPLICANT);
        member.setBirthDate(LocalDate.of(1970, 1, 1));
        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setAbbreviation("AA");
        member.setDateOfHire(LocalDate.EPOCH);
        member
                .setOrganisationUnit(OrganisationUnit.Builder
                        .builder()
                        .withId(ORGANISATION_UNIT_ID)
                        .withName(ORGANISATION_UNIT_NAME)
                        .build());
        return member;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calculation = new Calculation();
        calculation.setId(id);
        calculation.setMember(member);
        return calculation;
    }

    private Certificate createCertificate(Long id, Member member, String typeName) {
        CertificateType type = new CertificateType();
        type.setName(typeName);

        Certificate certificate = new Certificate();
        certificate.setId(id);
        certificate.setMember(member);
        certificate.setCertificateType(type);
        return certificate;
    }
}
