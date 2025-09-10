package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.model.example.Example;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class ExampleMapperIT {
    @InjectMocks
    private ExampleMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should return example")
    @Test
    void shouldReturnExample() {
        ExampleDto exampleDto = new ExampleDto(1L, "Nice Example");
        Example example = new Example(1L, "Nice Example");

        Example result = mapper.fromDto(exampleDto);
        assertEquals(example.toString(), result.toString());
    }

    @DisplayName("Should return example dto")
    @Test
    void shouldReturnExampleDto() {
        Example example = new Example(1L, "Nice Example");
        ExampleDto exampleDto = new ExampleDto(1L, "Nice Example");

        ExampleDto result = mapper.toDto(example);
        assertEquals(exampleDto.toString(), result.toString());
    }

    @DisplayName("Should return list of examples")
    @Test
    void shouldGetListOfExamples() {
        List<ExampleDto> exampleDtos = List
                .of(new ExampleDto(1L, "Nice Example"), new ExampleDto(2L, "Another Nice Example"));
        List<Example> examples = List.of(new Example(1L, "Nice Example"), new Example(2L, "Another Nice Example"));

        List<Example> result = mapper.fromDto(exampleDtos);
        assertEquals(examples.toString(), result.toString());
    }

    @DisplayName("Should return list of example dtos")
    @Test
    void shouldGetListOfExamplesDtos() {
        List<Example> examples = List.of(new Example(1L, "Nice Example"), new Example(2L, "Another Nice Example"));
        List<ExampleDto> exampleDtos = List
                .of(new ExampleDto(1L, "Nice Example"), new ExampleDto(2L, "Another Nice Example"));

        List<ExampleDto> result = mapper.toDto(examples);
        assertEquals(exampleDtos.toString(), result.toString());
    }
}
