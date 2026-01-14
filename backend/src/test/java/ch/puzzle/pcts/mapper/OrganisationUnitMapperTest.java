package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrganisationUnitMapperTest {

    private OrganisationUnitMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrganisationUnitMapper();
    }

    @DisplayName("Should map OrganisationUnit model to OrganisationUnitDto correctly")
    @Test
    void shouldReturnDto() {
        OrganisationUnitDto result = mapper.toDto(ORG_UNIT_1);

        assertEquals(ORG_UNIT_1_DTO, result);
    }

    @DisplayName("Should map OrganisationUnitDto to OrganisationUnit model correctly")
    @Test
    void shouldReturnModel() {
        OrganisationUnit result = mapper.fromDto(ORG_UNIT_1_DTO);

        assertEquals(ORG_UNIT_1_DTO.id(), result.getId());
        assertEquals(ORG_UNIT_1_DTO.name(), result.getName());
    }

    @DisplayName("Should map list of OrganisationUnit models to list of OrganisationUnitDtos")
    @Test
    void shouldReturnListOfDtos() {
        List<OrganisationUnit> models = List.of(ORG_UNIT_1, ORG_UNIT_2);

        List<OrganisationUnitDto> result = mapper.toDto(models);

        assertEquals(2, result.size());
        assertEquals(ORG_UNIT_1_DTO, result.getFirst());
        assertEquals(ORG_UNIT_2_DTO, result.getLast());
    }

    @DisplayName("Should map list of OrganisationUnitDtos to list of OrganisationUnit models")
    @Test
    void shouldReturnListOfModels() {
        List<OrganisationUnitDto> dtos = List.of(ORG_UNIT_1_DTO, ORG_UNIT_2_DTO);

        List<OrganisationUnit> result = mapper.fromDto(dtos);

        assertEquals(2, result.size());

        OrganisationUnit resultModel1 = result.get(0);
        assertEquals(ORG_UNIT_1_DTO.id(), resultModel1.getId());
        assertEquals(ORG_UNIT_1_DTO.name(), resultModel1.getName());

        OrganisationUnit resultModel2 = result.get(1);
        assertEquals(ORG_UNIT_2_DTO.id(), resultModel2.getId());
        assertEquals(ORG_UNIT_2_DTO.name(), resultModel2.getName());
    }

    @DisplayName("Should return null when mapping null model to dto")
    @Test
    void shouldReturnNullDtoForNullModel() {
        OrganisationUnitDto result = mapper.toDto((OrganisationUnit) null);
        assertNull(result);
    }
}
