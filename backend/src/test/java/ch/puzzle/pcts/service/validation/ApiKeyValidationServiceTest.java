package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.util.TestData.TOO_LONG_STRING;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.model.apikey.ApiKey;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiKeyValidationServiceTest extends ValidationBaseServiceTest<ApiKey, ApiKeyValidationService> {

    @Spy
    @InjectMocks
    ApiKeyValidationService validationService;

    @Override
    ApiKey getValidModel() {
        return createApiKey("Test Key", "$2a$10$validhashedkeyvalue");
    }

    @Override
    ApiKeyValidationService getService() {
        return validationService;
    }

    private static ApiKey createApiKey(String name, String hashedKey) {
        ApiKey apiKey = new ApiKey();
        apiKey.setName(name);
        apiKey.setHashedKey(hashedKey);
        return apiKey;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createApiKey(null, "$2a$10$valid"),
                            List.of(Map.of(FieldKey.CLASS, "ApiKey", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createApiKey("", "$2a$10$valid"),
                                List.of(Map.of(FieldKey.CLASS, "ApiKey", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createApiKey("S", "$2a$10$valid"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ApiKey",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.IS,
                                                    "S",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250"))),
                    Arguments
                            .of(createApiKey(TOO_LONG_STRING, "$2a$10$valid"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ApiKey",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING,
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250"))),
                    Arguments
                            .of(createApiKey("Valid Name", null),
                                List.of(Map.of(FieldKey.CLASS, "ApiKey", FieldKey.FIELD, "hashedKey"))),
                    Arguments
                            .of(createApiKey("Valid Name", ""),
                                List.of(Map.of(FieldKey.CLASS, "ApiKey", FieldKey.FIELD, "hashedKey"))),
                    Arguments
                            .of(createApiKey("Valid Name", "S"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "ApiKey",
                                                    FieldKey.FIELD,
                                                    "hashedKey",
                                                    FieldKey.IS,
                                                    "S",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250"))));
    }
}
