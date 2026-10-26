package ch.puzzle.pctsmigration.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DegreesApi;
import org.openapitools.client.model.DegreeDto;
import org.openapitools.client.model.DegreeInputDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeServiceTest {

    @Mock
    private DegreesApi degreesApi;

    @InjectMocks
    private DegreeService degreeService;

    @Test
    void testExecuteCreate() throws ApiException {
        DegreeInputDto input = new DegreeInputDto();
        DegreeDto expectedOutput = new DegreeDto();
        when(degreesApi.createNew(input)).thenReturn(expectedOutput);

        DegreeDto result = degreeService.executeCreate(input);

        assertEquals(expectedOutput, result);
        verify(degreesApi).createNew(input);
    }

    @Test
    void testExecuteDelete() throws ApiException {
        Long id = 123L;

        degreeService.executeDelete(id);

        verify(degreesApi).delete(id);
    }

    @Test
    void testExtractId() {
        DegreeDto entity = mock(DegreeDto.class);
        when(entity.getId()).thenReturn(99L);

        Long result = degreeService.extractId(entity);

        assertEquals(99L, result);
    }
}
