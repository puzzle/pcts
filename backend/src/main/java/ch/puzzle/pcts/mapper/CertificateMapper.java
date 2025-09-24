package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
        return new CertificateDto(model.getId(),
                                  model.getName(),
                                  model.getPoints(),
                                  model.getComment(),
                                  model.getTags().stream().map(Tag::getName).toList());
    }

    public Certificate fromDto(CertificateDto dto) {
        Set<Tag> rawTags = dto
                .tags()
                .stream()
                .flatMap(tagName -> Arrays.stream(tagName.split(",")))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(name -> new Tag(null, name))
                .collect(Collectors.toSet());

        return new Certificate(dto.id(), dto.name(), dto.points(), dto.comment(), rawTags);
    }
}
