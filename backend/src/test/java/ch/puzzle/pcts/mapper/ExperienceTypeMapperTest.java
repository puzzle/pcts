package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.EXP_TYPE_1;
import static ch.puzzle.pcts.util.TestDataModels.EXP_TYPE_2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ExperienceTypeMapper.class)
class ExperienceTypeMapperTest {
    private final List<ExperienceType> models = List.of(EXP_TYPE_1, EXP_TYPE_2);

    private final List<ExperienceTypeDto> dtos = List.of(EXP_TYPE_1_DTO, EXP_TYPE_2_DTO);

    @Autowired
    private ExperienceTypeMapper mapper;

    @DisplayName("Should return experience type")
    @Test
    void shouldReturnExperienceType() {
        ExperienceType result = mapper.fromDto(EXP_TYPE_1_DTO);
        assertEquals(EXP_TYPE_1, result);
    }

    @DisplayName("Should return experience type dto")
    @Test
    void shouldReturnExperienceTypeDto() {
        ExperienceTypeDto result = mapper.toDto(EXP_TYPE_1);
        assertEquals(EXP_TYPE_1_DTO, result);
    }

    @DisplayName("Should return list of experience types")
    @Test
    void shouldGetListOfExperienceTypes() {
        List<ExperienceType> result = mapper.fromDto(dtos);
        assertEquals(models, result);
    }

    @DisplayName("Should return list of experience type dtos")
    @Test
    void shouldGetListOfExperienceTypeDtos() {
        List<ExperienceTypeDto> result = mapper.toDto(models);
        assertEquals(dtos, result);
    }
}
