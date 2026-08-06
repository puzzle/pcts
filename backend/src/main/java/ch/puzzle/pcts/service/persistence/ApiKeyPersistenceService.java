package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.API_KEY;

import ch.puzzle.pcts.model.apikey.ApiKey;
import ch.puzzle.pcts.repository.ApiKeyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyPersistenceService extends PersistenceBase<ApiKey, ApiKeyRepository> {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyPersistenceService(ApiKeyRepository apiKeyRepository) {
        super(apiKeyRepository);
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public String entityName() {
        return API_KEY;
    }

    public List<ApiKey> findAllActive() {
        return apiKeyRepository.findAllByRevokedFalse();
    }
}
