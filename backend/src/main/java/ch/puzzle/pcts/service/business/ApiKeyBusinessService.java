package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.apikey.ApiKey;
import ch.puzzle.pcts.service.persistence.ApiKeyPersistenceService;
import ch.puzzle.pcts.service.validation.ApiKeyValidationService;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyBusinessService extends BusinessBase<ApiKey> {
    private final ApiKeyPersistenceService apiKeyPersistenceService;
    private final PasswordEncoder passwordEncoder;

    public ApiKeyBusinessService(ApiKeyValidationService validationService, ApiKeyPersistenceService persistenceService,
                                 PasswordEncoder passwordEncoder) {
        super(validationService, persistenceService);
        this.apiKeyPersistenceService = persistenceService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<ApiKey> isValid(String rawKey) {
        if (rawKey == null) {
            return Optional.empty();
        }

        return apiKeyPersistenceService
                .findAllActive()
                .stream()
                .filter(key -> passwordEncoder.matches(rawKey, key.getHashedKey()))
                .findFirst();
    }

}
