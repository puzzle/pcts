package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrganisationUnitMapper {

    public List<OrganisationUnitDto> toDto(List<OrganisationUnit> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<OrganisationUnit> fromDto(List<OrganisationUnitDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public OrganisationUnitDto toDto(OrganisationUnit model) {
        if (model == null) {
            return null;
        }
        return new OrganisationUnitDto(model.getId(), model.getName());
    }

    public OrganisationUnit fromDto(OrganisationUnitDto dto) {
        return OrganisationUnit.Builder.builder().withId(dto.id()).withName(dto.name()).build();
    }
}
