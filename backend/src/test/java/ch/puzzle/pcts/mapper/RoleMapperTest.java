package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.ROLE_2;
import static ch.puzzle.pcts.util.TestDataModels.ROLE_3;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoleMapper.class)
class RoleMapperTest {
    private final List<Role> modelList = List.of(ROLE_3, ROLE_2);

    private final Set<Role> modelSet = Set.of(ROLE_3, ROLE_2);

    private final List<RoleDto> dtoList = List.of(ROLE_3_DTO, ROLE_2_DTO);

    private final Set<RoleDto> dtoSet = Set.of(ROLE_3_DTO, ROLE_2_DTO);

    @Autowired
    private RoleMapper mapper;

    @DisplayName("Should return role")
    @Test
    void shouldReturnRole() {
        Role result = mapper.fromDto(ROLE_2_DTO);
        assertEquals(ROLE_2, result);
    }

    @DisplayName("Should return role dto")
    @Test
    void shouldReturnRoleDto() {
        RoleDto result = mapper.toDto(ROLE_2);
        assertEquals(ROLE_2_DTO, result);
    }

    @DisplayName("Should return list of roles")
    @Test
    void shouldGetListOfRoles() {
        List<Role> result = dtoList.stream().map(mapper::fromDto).toList();
        assertEquals(modelList, result);
    }

    @DisplayName("Should return list of role dtos")
    @Test
    void shouldGetListOfRoleDtos() {
        List<RoleDto> result = mapper.toDto(modelList);
        assertEquals(dtoList, result);
    }

    @DisplayName("Should map roles to dtos")
    @Test
    void shouldMapRolesToDto() {
        Set<RoleDto> result = mapper.toDto(modelSet);
        assertEquals(dtoSet, result);
    }
}
