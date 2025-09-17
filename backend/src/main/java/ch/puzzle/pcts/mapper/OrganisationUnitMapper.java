package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.organisation_unit.OrganisationUnitDto;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
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
        return new OrganisationUnitDto(model.getId(), model.getName());
    }

    public OrganisationUnit fromDto(OrganisationUnitDto dto) {
        return new OrganisationUnit(dto.id(), dto.name());
    }
}
