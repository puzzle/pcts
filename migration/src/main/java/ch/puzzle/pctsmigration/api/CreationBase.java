package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;

public abstract class CreationBase<I, D> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void create(List<I> dtos) {
        List<Long> createdIds = new ArrayList<>();

        for (I dto : dtos) {
            try {
                D created = executeCreate(dto);
                createdIds.add(extractId(created));
            } catch (ApiException e) {
                rollbackCreatedEntities(createdIds);
                throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                       "Migration aborted. Reason: " + e.getMessage()));
            }
        }
    }

    private void rollbackCreatedEntities(List<Long> createdIds) {
        for (Long id : createdIds) {
            try {
                executeDelete(id);
            } catch (ApiException rollbackException) {
                this.logger.error("Rollback failed for ID {}", id, rollbackException);
            }
        }
    }

    protected abstract D executeCreate(I dto) throws ApiException;

    protected abstract void executeDelete(Long id) throws ApiException;

    protected abstract Long extractId(D entity);
}
