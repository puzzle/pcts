package ch.puzzle.pcts.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.business.ExampleBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ExampleControllerTest {

    @Mock
    private ExampleMapper mapper;

    @Mock
    private ExampleBusinessService service;

    @InjectMocks
    private ExampleController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExample_shouldReturnMappedList() {
        Example example = new Example();
        ExampleDto dto = new ExampleDto(null, "Example 1");

        when(service.getAll()).thenReturn(List.of(example));
        when(mapper.toDto(List.of(example))).thenReturn(List.of(dto));

        ResponseEntity<List<ExampleDto>> response = controller.getExample();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertSame(dto, response.getBody().get(0));

        verify(service).getAll();
        verify(mapper).toDto(List.of(example));
    }

    @Test
    void getExampleById_shouldReturnMappedDto() {
        long id = 42L;
        Example example = new Example();
        ExampleDto dto = new ExampleDto(null, "Example 1");

        when(service.getById(id)).thenReturn(example);
        when(mapper.toDto(example)).thenReturn(dto);

        ResponseEntity<ExampleDto> response = controller.getExampleById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(dto, response.getBody());

        verify(service).getById(id);
        verify(mapper).toDto(example);
    }

    @Test
    void createNew_shouldReturnCreatedDto() {
        ExampleDto inputDto = new ExampleDto(null, "Example 1");
        Example example = new Example();
        Example createdExample = new Example();
        ExampleDto outputDto = new ExampleDto(null, "Example 1");

        when(mapper.fromDto(inputDto)).thenReturn(example);
        when(service.create(example)).thenReturn(createdExample);
        when(mapper.toDto(createdExample)).thenReturn(outputDto);

        ResponseEntity<ExampleDto> response = controller.createNew(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(outputDto, response.getBody());

        verify(mapper).fromDto(inputDto);
        verify(service).create(example);
        verify(mapper).toDto(createdExample);
    }
}
