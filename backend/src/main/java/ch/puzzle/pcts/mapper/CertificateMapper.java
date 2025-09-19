package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public List<CertificateDto> toDto(List<Certificate> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Certificate> fromDto(List<CertificateDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public CertificateDto toDto(Certificate model) {
        return new CertificateDto(model.getId(), model.getName(), model.getPoints(), model.getComment());
    }

    public Certificate fromDto(CertificateDto dto) {
        return new Certificate(dto.id(), dto.name(), dto.points(), dto.comment());
    }
}
