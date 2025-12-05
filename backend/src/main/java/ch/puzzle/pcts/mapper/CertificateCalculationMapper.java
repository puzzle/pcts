package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CertificateCalculationMapper {

    private final CertificateBusinessService certificateBusinessService;

    public CertificateCalculationMapper(CertificateBusinessService certificateBusinessService) {
        this.certificateBusinessService = certificateBusinessService;
    }

    public List<CertificateCalculation> fromDto(List<Long> ids) {
        return ids.stream().map(this::fromDto).toList();
    }

    public CertificateCalculation fromDto(Long id) {
        return new CertificateCalculation(null, null, certificateBusinessService.getById(id));
    }
}
