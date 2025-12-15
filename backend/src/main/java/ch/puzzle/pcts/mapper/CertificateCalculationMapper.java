package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CertificateCalculationMapper {

    private final CertificateBusinessService certificateBusinessService;
    private final CertificateMapper certificateMapper;

    public CertificateCalculationMapper(CertificateBusinessService certificateBusinessService,
                                        CertificateMapper certificateMapper) {
        this.certificateBusinessService = certificateBusinessService;
        this.certificateMapper = certificateMapper;

    }

    public List<CertificateCalculationDto> toDto(List<CertificateCalculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<CertificateCalculation> fromDto(List<Long> ids) {
        return ids.stream().map(this::fromDto).toList();
    }

    public CertificateCalculation fromDto(Long id) {
        return new CertificateCalculation(null, null, certificateBusinessService.getById(id));
    }

    public CertificateCalculationDto toDto(CertificateCalculation model) {
        return new CertificateCalculationDto(model.getId(), certificateMapper.toDto(model.getCertificate()));
    }
}
