package ch.puzzle.pcts;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.puzzle.pcts.util.IT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import tools.jackson.databind.json.JsonMapper;

@IT
@AutoConfigureMockMvc
class ApplicationAvailabilityTest {
    private static final String BASEURL = "/actuator/health/";
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @DisplayName("Should be healthy")
    @Test
    void shouldBeHealthy() throws Exception {
        mvc
                .perform(get(BASEURL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(content().string("{\"groups\":[\"liveness\",\"readiness\"],\"status\":\"UP\"}"));
    }

    @DisplayName("Should accept traffic")
    @Test
    void shouldAcceptTraffic() throws Exception {
        mvc
                .perform(get(BASEURL + "readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(content().string("{\"status\":\"UP\"}"));
    }

    @DisplayName("Should be live")
    @Test
    void shouldBeLive() throws Exception {
        mvc
                .perform(get(BASEURL + "liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(content().string("{\"status\":\"UP\"}"));
    }
}
