package ch.puzzle.pctsmigration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiException;

class CreationBaseTest {

    static class DummyInput {
    }
    static class DummyEntity {
        Long id;
        DummyEntity(Long id) {
            this.id = id;
        }
    }

    interface DummyApi {
        DummyEntity create(DummyInput input) throws ApiException;
        void delete(Long id) throws ApiException;
    }

    static class DummyService extends CreationBase<DummyInput, DummyEntity> {
        private final DummyApi api;

        DummyService(DummyApi api) {
            this.api = api;
        }

        @Override
        protected DummyEntity executeCreate(DummyInput dto) throws ApiException {
            return api.create(dto);
        }

        @Override
        protected void executeDelete(Long id) throws ApiException {
            api.delete(id);
        }

        @Override
        protected Long extractId(DummyEntity entity) {
            return entity.id;
        }
    }

    private DummyApi apiMock;
    private DummyService service;

    @BeforeEach
    void setUp() {
        apiMock = mock(DummyApi.class);
        service = new DummyService(apiMock);
    }

    @Test
    void testCreate_Success() throws ApiException {
        DummyInput in1 = new DummyInput();
        DummyInput in2 = new DummyInput();

        when(apiMock.create(in1)).thenReturn(new DummyEntity(1L));
        when(apiMock.create(in2)).thenReturn(new DummyEntity(2L));

        service.create(Arrays.asList(in1, in2));

        verify(apiMock).create(in1);
        verify(apiMock).create(in2);
        verify(apiMock, never()).delete(anyLong());
    }

    @Test
    void testCreate_ThrowsApiException_TriggersRollback() throws ApiException {
        DummyInput in1 = new DummyInput();
        DummyInput in2 = new DummyInput();

        when(apiMock.create(in1)).thenReturn(new DummyEntity(1L));
        when(apiMock.create(in2)).thenThrow(new ApiException("API Error"));

        MigrationException ex = assertThrows(MigrationException.class, () -> service.create(Arrays.asList(in1, in2)));

        assertEquals("Migration aborted. Reason: API Error", ex.getError().message());
        verify(apiMock).delete(1L);
        verify(apiMock, never()).delete(2L);
    }

    @Test
    void testCreate_RollbackFails_LogsErrorAndStillThrowsMigrationException() throws ApiException {
        DummyInput in1 = new DummyInput();
        DummyInput in2 = new DummyInput();

        when(apiMock.create(in1)).thenReturn(new DummyEntity(1L));
        when(apiMock.create(in2)).thenThrow(new ApiException("API Error"));
        doThrow(new ApiException("Rollback Error")).when(apiMock).delete(1L);

        MigrationException ex = assertThrows(MigrationException.class, () -> service.create(Arrays.asList(in1, in2)));

        assertEquals("Migration aborted. Reason: API Error", ex.getError().message());
        verify(apiMock).delete(1L);
    }
}