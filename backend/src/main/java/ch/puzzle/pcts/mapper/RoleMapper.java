package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.role.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public List<RoleDto> toDto(List<Role> models) {
        return models.stream().map(this::toDto).toList();
    }

    public Set<RoleDto> toDto(Set<Role> models) {
        return models.stream().map(this::toDto).collect(Collectors.toSet());
    }

    public RoleDto toDto(Role model) {
        return new RoleDto(model.getId(), model.getName(), model.getIsManagement());
    }

    public Role fromDto(RoleDto dto) {
        return Role.Builder
                .builder()
                .withId(dto.id())
                .withName(dto.name())
                .withIsManagement(dto.isManagement())
                .build();
    }
}
