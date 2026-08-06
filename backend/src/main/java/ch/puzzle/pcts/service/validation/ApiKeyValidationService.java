package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.model.apikey.ApiKey;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyValidationService extends ValidationBase<ApiKey> {

    public ApiKeyValidationService() {
    }

    @Override
    public void validateOnCreate(ApiKey apiKey) {
        super.validateOnCreate(apiKey);
    }

    @Override
    public void validateOnUpdate(Long id, ApiKey apiKey) {
        super.validateOnUpdate(id, apiKey);
    }
}
