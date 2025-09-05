package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public List<RoleDto> toDto(List<Role> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Role> fromDto(List<RoleDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public RoleDto toDto(Role model) {
        return new RoleDto(model.getId(), model.getName(), model.getDeleted_at());
    }

    public Role fromDto(RoleDto dto) {
        return new Role(dto.id(), dto.name(), dto.deleted_at());
    }
}
