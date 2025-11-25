package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrganisationUnitMapperTest {

    private OrganisationUnitMapper mapper;

    private OrganisationUnit model1;
    private OrganisationUnit model2;

    private OrganisationUnitDto dto1;
    private OrganisationUnitDto dto2;

    @BeforeEach
    void setUp() {
        mapper = new OrganisationUnitMapper();

        model1 = new OrganisationUnit(1L, "Unit A");
        model2 = new OrganisationUnit(2L, "Unit B");

        dto1 = new OrganisationUnitDto(1L, "Unit A");
        dto2 = new OrganisationUnitDto(2L, "Unit B");
    }

    @DisplayName("Should map OrganisationUnit model to OrganisationUnitDto correctly")
    @Test
    void shouldReturnDto() {
        OrganisationUnitDto result = mapper.toDto(model1);

        assertEquals(model1.getId(), result.id());
        assertEquals(model1.getName(), result.name());
    }

    @DisplayName("Should map OrganisationUnitDto to OrganisationUnit model correctly")
    @Test
    void shouldReturnModel() {
        OrganisationUnit result = mapper.fromDto(dto1);

        assertEquals(dto1.id(), result.getId());
        assertEquals(dto1.name(), result.getName());
    }

    @DisplayName("Should map list of OrganisationUnit models to list of OrganisationUnitDtos")
    @Test
    void shouldReturnListOfDtos() {
        List<OrganisationUnit> models = List.of(model1, model2);

        List<OrganisationUnitDto> result = mapper.toDto(models);

        assertEquals(2, result.size());

        OrganisationUnitDto resultDto1 = result.getFirst();
        assertEquals(model1.getId(), resultDto1.id());
        assertEquals(model1.getName(), resultDto1.name());

        OrganisationUnitDto resultDto2 = result.get(1);
        assertEquals(model2.getId(), resultDto2.id());
        assertEquals(model2.getName(), resultDto2.name());
    }

    @DisplayName("Should map list of OrganisationUnitDtos to list of OrganisationUnit models")
    @Test
    void shouldReturnListOfModels() {
        List<OrganisationUnitDto> dtos = List.of(dto1, dto2);

        List<OrganisationUnit> result = mapper.fromDto(dtos);

        assertEquals(2, result.size());

        OrganisationUnit resultModel1 = result.get(0);
        assertEquals(dto1.id(), resultModel1.getId());
        assertEquals(dto1.name(), resultModel1.getName());

        OrganisationUnit resultModel2 = result.get(1);
        assertEquals(dto2.id(), resultModel2.getId());
        assertEquals(dto2.name(), resultModel2.getName());
    }

    @DisplayName("Should return null when mapping null model to dto")
    @Test
    void shouldReturnNullDtoForNullModel() {
        OrganisationUnitDto result = mapper.toDto((OrganisationUnit) null);
        assertNull(result);
    }
}
