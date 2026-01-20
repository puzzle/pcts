package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPE_1;
import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPE_2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DegreeTypeMapper.class)
class DegreeTypeMapperTest {
    private final List<DegreeType> models = List.of(DEGREE_TYPE_1, DEGREE_TYPE_2);

    private final List<DegreeTypeDto> dtos = List.of(DEGREE_TYPE_1_DTO, DEGREE_TYPE_2_DTO);

    @Autowired
    private DegreeTypeMapper mapper;

    @DisplayName("Should return degree type")
    @Test
    void shouldReturnDegreeType() {
        DegreeType result = mapper.fromDto(DEGREE_TYPE_1_DTO);
        assertEquals(DEGREE_TYPE_1, result);
    }

    @DisplayName("Should return degree type dto")
    @Test
    void shouldReturnDegreeTypeDto() {
        DegreeTypeDto result = mapper.toDto(DEGREE_TYPE_1);
        assertEquals(DEGREE_TYPE_1_DTO, result);
    }

    @DisplayName("Should return list of degree types")
    @Test
    void shouldGetListOfDegreeTypes() {
        List<DegreeType> result = mapper.fromDto(dtos);
        assertEquals(models, result);
    }

    @DisplayName("Should return list of degree type dtos")
    @Test
    void shouldGetListOfDegreeTypeDtos() {
        List<DegreeTypeDto> result = mapper.toDto(models);
        assertEquals(dtos, result);
    }
}
