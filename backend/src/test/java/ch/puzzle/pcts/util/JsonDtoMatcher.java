package ch.puzzle.pcts.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

public class JsonDtoMatcher {

    private static final JsonMapper jsonMapper = new JsonMapper();

    public static ResultMatcher matchesDto(Object expectedDto, String jsonPrefix) {
        return result -> {
            JsonNode expectedNode = jsonMapper.valueToTree(expectedDto);
            matchJson(expectedNode, jsonPrefix, result);
        };
    }

    private static void matchJson(JsonNode expected, String pathPrefix, MvcResult result) throws Exception {
        if (expected == null || expected.isNull())
            return;
        switch (expected.getNodeType()) {
            case OBJECT -> {
                for (String propertyName : expected.propertyNames()) {
                    matchJson(expected.get(propertyName), pathPrefix + "." + propertyName, result);
                }
            }
            case ARRAY -> {
                for (int i = 0; i < expected.size(); i++) {
                    matchJson(expected.get(i), pathPrefix + "[" + i + "]", result);
                }
            }
            case BOOLEAN -> jsonPath(pathPrefix).value(expected.booleanValue()).match(result);
            case NUMBER -> jsonPath(pathPrefix).value(expected.decimalValue()).match(result);
            case NULL -> jsonPath(pathPrefix).doesNotExist().match(result);
            default -> jsonPath(pathPrefix).value(expected.asString()).match(result);
        }
    }
}
