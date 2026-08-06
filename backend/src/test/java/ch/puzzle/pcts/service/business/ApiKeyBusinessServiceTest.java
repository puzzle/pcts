package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.apikey.ApiKey;
import ch.puzzle.pcts.service.persistence.ApiKeyPersistenceService;
import ch.puzzle.pcts.service.validation.ApiKeyValidationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ApiKeyBusinessServiceTest
        extends
            BaseBusinessTest<ApiKey, ApiKeyPersistenceService, ApiKeyValidationService, ApiKeyBusinessService> {

    @Mock
    private ApiKey apiKey;

    @Mock
    private List<ApiKey> apiKeys;

    @Mock
    private ApiKeyPersistenceService persistenceService;

    @Mock
    private ApiKeyValidationService validationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ApiKeyBusinessService businessService;

    @Override
    ApiKey getModel() {
        return apiKey;
    }

    @Override
    ApiKeyPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    ApiKeyValidationService getValidationService() {
        return validationService;
    }

    @Override
    ApiKeyBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should return empty when raw key is null or empty")
    @ParameterizedTest
    @NullAndEmptySource
    void isValidShouldReturnEmptyWhenRawKeyIsNullOrEmpty(String value) {
        Optional<ApiKey> result = businessService.isValid(value);

        assertTrue(result.isEmpty());
        verifyNoInteractions(persistenceService);
    }

    @DisplayName("Should return matching API key when raw key matches")
    @Test
    void isValidShouldReturnApiKeyWhenKeyMatches() {
        ApiKey key = new ApiKey();
        key.setHashedKey("$2a$10$hashed");
        when(persistenceService.findAllActive()).thenReturn(List.of(key));
        when(passwordEncoder.matches("rawKey", "$2a$10$hashed")).thenReturn(true);

        Optional<ApiKey> result = businessService.isValid("rawKey");

        assertTrue(result.isPresent());
        assertEquals(key, result.get());
    }

    @DisplayName("Should return empty when no active key matches")
    @Test
    void isValidShouldReturnEmptyWhenNoKeyMatches() {
        ApiKey key = new ApiKey();
        key.setHashedKey("$2a$10$hashed");
        when(persistenceService.findAllActive()).thenReturn(List.of(key));
        when(passwordEncoder.matches("wrongKey", "$2a$10$hashed")).thenReturn(false);

        Optional<ApiKey> result = businessService.isValid("wrongKey");

        assertTrue(result.isEmpty());
    }
}
