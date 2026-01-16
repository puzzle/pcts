package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.repository.CertificateCalculationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CertificateCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<CertificateCalculation, CertificateCalculationRepository, CertificateCalculationPersistenceService> {

    @Autowired
    CertificateCalculationPersistenceServiceIT(CertificateCalculationPersistenceService service) {
        super(service);
    }

    @Override
    CertificateCalculation getModel() {
        return new CertificateCalculation(null, CALCULATION_1, CERTIFICATE_1);
    }

    @Override
    List<CertificateCalculation> getAll() {
        return CERTIFICATE_CALCULATIONS;
    }

    @DisplayName("Should fetch CertificateCalculations by calculationId")
    @Transactional
    @Test
    void shouldGetByCalculationId() {
        CertificateCalculation cc = persistenceService.save(getModel());

        List<CertificateCalculation> result = persistenceService.getByCalculationId(cc.getCalculation().getId());

        assertThat(result).contains(cc);
    }

    @DisplayName("Should fetch CertificateCalculations by certificateId")
    @Transactional
    @Test
    void shouldGetByCertificateId() {
        CertificateCalculation cc = persistenceService.save(getModel());

        List<CertificateCalculation> result = persistenceService.getByCertificateId(cc.getCertificate().getId());

        assertThat(result).contains(cc);
    }

    @DisplayName("Should save and retrieve CertificateCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        CertificateCalculation cc = getModel();
        CertificateCalculation saved = persistenceService.save(cc);

        assertEquals(cc.getCalculation(), saved.getCalculation());
        assertEquals(cc.getCertificate(), saved.getCertificate());
        assertThat(persistenceService.getAll()).contains(saved);
    }

    @DisplayName("Should delete all CertificateCalculations by IDs in batch")
    @Transactional
    @Test
    void shouldDeleteAllByIdInBatch() {
        CertificateCalculation cc1 = persistenceService.save(getAll().get(0));
        CertificateCalculation cc2 = persistenceService.save(getAll().get(1));
        CertificateCalculation cc3 = persistenceService.save(getAll().get(2));

        List<Long> idsToDelete = List.of(cc1.getId(), cc3.getId());

        persistenceService.deleteAllByIdInBatch(idsToDelete);

        List<CertificateCalculation> remaining = persistenceService.getAll();

        assertThat(remaining).hasSize(1);
        assertThat(remaining.getFirst().getId()).isEqualTo(cc2.getId());
    }
}
