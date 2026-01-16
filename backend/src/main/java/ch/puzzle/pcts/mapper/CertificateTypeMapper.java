package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CertificateTypeMapper {

    public List<CertificateTypeDto> toDto(List<CertificateType> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<CertificateType> fromDto(List<CertificateTypeDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public CertificateTypeDto toDto(CertificateType model) {
        return new CertificateTypeDto(model.getId(),
                                      model.getName(),
                                      model.getPoints(),
                                      model.getComment(),
                                      model.getTags() == null ? List.of()
                                              : model.getTags().stream().map(Tag::getName).toList());
    }

    public CertificateType fromDto(CertificateTypeDto dto) {
        Set<Tag> rawTags = dto.tags() == null ? Set.of()
                : dto
                        .tags()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(name -> !name.isBlank())
                        .flatMap(tagName -> Arrays.stream(tagName.split(",")))
                        .map(String::trim)
                        .map(name -> Tag.Builder.builder().withName(name).build())
                        .collect(Collectors.toCollection(LinkedHashSet::new));

        return CertificateType.Builder.builder()
                .withId(dto.id())
                .withName(dto.name())
                .withPoints(dto.points())
                .withComment(dto.comment())
                .withTags(rawTags)
                .build();
    }
}
