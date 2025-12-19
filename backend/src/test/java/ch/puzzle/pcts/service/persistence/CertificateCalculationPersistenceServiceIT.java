package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateCalculationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CertificateCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<CertificateCalculation, CertificateCalculationRepository, CertificateCalculationPersistenceService> {

    CalculationPersistenceServiceIT calculationServiceIT;
    CertificatePersistenceServiceIT certificateServiceIT;

    @Autowired
    CertificateCalculationPersistenceServiceIT(CertificateCalculationPersistenceService service,
                                               CalculationPersistenceService calculationService,
                                               CertificatePersistenceService certificateService) {
        super(service);
        this.calculationServiceIT = new CalculationPersistenceServiceIT(calculationService, null, null);
        this.certificateServiceIT = new CertificatePersistenceServiceIT(certificateService);
    }

    @Override
    CertificateCalculation getModel() {
        Calculation calculation = calculationServiceIT.getAll().get(0);
        Certificate certificate = certificateServiceIT.getAll().get(0);

        return new CertificateCalculation(null, calculation, certificate);
    }

    @Override
    List<CertificateCalculation> getAll() {
        List<Calculation> calculations = calculationServiceIT.getAll();
        List<Certificate> certificates = certificateServiceIT.getAll();

        List<CertificateCalculation> list = new ArrayList<>();

        list.add(new CertificateCalculation(1L, calculations.get(0), certificates.get(1)));
        list.add(new CertificateCalculation(2L, calculations.get(1), certificates.get(1)));

        list.add(new CertificateCalculation(3L, calculations.get(0), certificates.get(4)));
        return list;
    }

    @DisplayName("Should fetch CertificateCalculations by calculationId")
    @Transactional
    @Test
    void shouldGetByCalculationId() {
        CertificateCalculation cc = service.save(getModel());

        List<CertificateCalculation> result = service.getByCalculationId(cc.getCalculation().getId());

        assertThat(result).contains(cc);
    }

    @DisplayName("Should fetch CertificateCalculations by certificateId")
    @Transactional
    @Test
    void shouldGetByCertificateId() {
        CertificateCalculation cc = service.save(getModel());

        List<CertificateCalculation> result = service.getByCertificateId(cc.getCertificate().getId());

        assertThat(result).contains(cc);
    }

    @DisplayName("Should save and retrieve CertificateCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        CertificateCalculation cc = getModel();
        CertificateCalculation saved = service.save(cc);

        assertEquals(cc.getCalculation(), saved.getCalculation());
        assertEquals(cc.getCertificate(), saved.getCertificate());
        assertThat(service.getAll()).contains(saved);
    }
}
