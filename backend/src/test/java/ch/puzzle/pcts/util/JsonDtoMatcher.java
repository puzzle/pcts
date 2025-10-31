package ch.puzzle.pcts.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Iterator;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

public class JsonDtoMatcher {

    private static final ObjectMapper objectMapper = createCustomObjectMapper();

    private static ObjectMapper createCustomObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    public static ResultMatcher matchesDto(Object expectedDto, String jsonPrefix) {
        return result -> {
            JsonNode expectedNode = objectMapper.valueToTree(expectedDto);
            matchJson(expectedNode, jsonPrefix, result);
        };
    }

    private static void matchJson(JsonNode expected, String pathPrefix, MvcResult result) throws Exception {
        if (expected == null || expected.isNull())
            return;
        switch (expected.getNodeType()) {
            case OBJECT -> {
                Iterator<String> fieldNames = expected.fieldNames();
                while (fieldNames.hasNext()) {
                    String field = fieldNames.next();
                    matchJson(expected.get(field), pathPrefix + "." + field, result);
                }
            }
            case ARRAY -> {
                for (int i = 0; i < expected.size(); i++) {
                    matchJson(expected.get(i), pathPrefix + "[" + i + "]", result);
                }
            }
            case BOOLEAN -> jsonPath(pathPrefix).value(expected.booleanValue()).match(result);
            case NUMBER -> jsonPath(pathPrefix).value(expected.longValue()).match(result);
            case NULL -> jsonPath(pathPrefix).doesNotExist().match(result);
            default -> jsonPath(pathPrefix).value(expected.asText()).match(result);
        }
    }
}
