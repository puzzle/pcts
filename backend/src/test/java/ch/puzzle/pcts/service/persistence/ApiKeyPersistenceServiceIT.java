package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.API_KEYS;
import static ch.puzzle.pcts.util.TestDataModels.API_KEY_1;
import static ch.puzzle.pcts.util.TestDataModels.API_KEY_2;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.apikey.ApiKey;
import ch.puzzle.pcts.repository.ApiKeyRepository;
import ch.puzzle.pcts.service.JwtService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class ApiKeyPersistenceServiceIT extends PersistenceBaseIT<ApiKey, ApiKeyRepository, ApiKeyPersistenceService> {

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    ApiKeyPersistenceServiceIT(ApiKeyPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    ApiKey getModel() {
        ApiKey key = new ApiKey();
        key.setName("New Test Key");
        key.setHashedKey("$2a$10$testhashedkeyvalue");
        key.setRevoked(false);
        return key;
    }

    @Override
    List<ApiKey> getAll() {
        return API_KEYS;
    }

    @DisplayName("Should return only non-revoked API keys")
    @Test
    void shouldFindAllActive() {
        List<ApiKey> result = persistenceService.findAllActive();

        assertThat(result).hasSize(API_KEYS.size());
        assertThat(result).contains(API_KEY_1);
        assertThat(result).contains(API_KEY_2);
    }
}
