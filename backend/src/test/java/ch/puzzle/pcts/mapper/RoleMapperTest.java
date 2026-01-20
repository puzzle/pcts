package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.ROLE_1;
import static ch.puzzle.pcts.util.TestDataModels.ROLE_2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RoleMapper.class)
class RoleMapperTest {
    private final List<Role> models = List.of(ROLE_1, ROLE_2);

    private final List<RoleDto> dtos = List.of(ROLE_1_DTO, ROLE_2_DTO);

    @Autowired
    private RoleMapper mapper;

    @DisplayName("Should return role")
    @Test
    void shouldReturnRole() {
        Role result = mapper.fromDto(ROLE_1_DTO);
        assertEquals(ROLE_1, result);
    }

    @DisplayName("Should return role dto")
    @Test
    void shouldReturnRoleDto() {
        RoleDto result = mapper.toDto(ROLE_1);
        assertEquals(ROLE_1_DTO, result);
    }

    @DisplayName("Should return list of roles")
    @Test
    void shouldGetListOfRoles() {
        List<Role> result = mapper.fromDto(dtos);
        assertEquals(models, result);
    }

    @DisplayName("Should return list of role dtos")
    @Test
    void shouldGetListOfRoleDtos() {
        List<RoleDto> result = mapper.toDto(models);
        assertEquals(dtos, result);
    }
}
