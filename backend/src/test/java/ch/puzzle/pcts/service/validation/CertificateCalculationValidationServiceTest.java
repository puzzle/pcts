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

    @Override
    CertificateCalculationValidationService getService() {
        return new CertificateCalculationValidationService();
    }

    @Override
    CertificateCalculation getValidModel() {
        Member member = createMember(1L, "Alice", "Smith");
        Calculation calculation = createCalculation(1L, member);
        Certificate certificate = createCertificate(1L, member, "Certificate A");

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
        Member member2 = createMember(2L, "Bob", "Johnson");

        CertificateCalculation cc = getValidModel();
        cc.getCalculation().setMember(member2);

        PCTSException exception = assertThrows(PCTSException.class, () -> spyService.validateMemberForCalculation(cc));

        assertEquals(ErrorKey.ATTRIBUTE_MATCHES, exception.getErrorKeys().get(0));
        assertEquals(Map
                .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "certificate", FieldKey.CONDITION_FIELD, "member"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should throw exception on duplicate certificate ID")
    @Test
    void shouldThrowExceptionOnDuplicateCertificateId() {
        CertificateCalculationValidationService spyService = spy(getService());
        CertificateCalculation cc = getValidModel();
        cc.getCertificate().getCertificateType().setName("Certificate A");

        List<CertificateCalculation> existing = List.of(cc);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> spyService.validateDuplicateCertificateId(cc, existing));

        assertEquals(ErrorKey.DUPLICATE_CALCULATION, exception.getErrorKeys().get(0));
        assertEquals(Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "certificate", FieldKey.IS, "Certificate A"),
                     exception.getErrorAttributes().get(0));
    }

    @DisplayName("Should call validateMemberForCalculation on validateOnCreate")
    @Test
    void shouldCallValidateMemberForCalculationOnCreate() {
        CertificateCalculationValidationService spyService = spy(getService());
        CertificateCalculation cc = getValidModel();

        doNothing().when(spyService).validateMemberForCalculation(any());

        spyService.validateOnCreate(cc);

        verify(spyService).validateMemberForCalculation(cc);
    }

    private Member createMember(Long id, String firstName, String lastName) {
        Member m = new Member();
        m.setId(id);
        m.setEmploymentState(EmploymentState.APPLICANT);
        m.setBirthDate(LocalDate.of(1970, 1, 1));
        m.setFirstName(firstName);
        m.setLastName(lastName);
        m.setAbbreviation("AA");
        m.setDateOfHire(LocalDate.EPOCH);
        m.setOrganisationUnit(new OrganisationUnit(1L, "Organisation Unit"));
        return m;
    }

    private Calculation createCalculation(Long id, Member member) {
        Calculation calc = new Calculation();
        calc.setId(id);
        calc.setMember(member);
        return calc;
    }

    private Certificate createCertificate(Long id, Member member, String typeName) {
        CertificateType type = new CertificateType();
        type.setName(typeName);

        Certificate cert = new Certificate();
        cert.setId(id);
        cert.setMember(member);
        cert.setCertificateType(type);
        return cert;
    }
}
