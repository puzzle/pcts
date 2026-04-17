package ch.puzzle.pcts.service.scheduled;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.dto.puzzletime.EmployeeAttributes;
import ch.puzzle.pcts.dto.puzzletime.EmployeeData;
import ch.puzzle.pcts.dto.puzzletime.PuzzleTimeResponseDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.util.IT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

@IT
@TestPropertySource(properties = { "app.member-sync.enabled=true",
        "app.member-sync.url=${wiremock.base-url}/api/employees", "app.member-sync.username=testuser",
        "app.member-sync.password=testpass" })
class MemberAttributesSyncJobIT {
    @Value("${wiremock.base-url}")
    private String wiremockBaseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Autowired
    private MemberAttributesSyncJob syncJob;

    @Autowired
    private MemberBusinessService memberBusinessService;

    @BeforeEach
    void setUpWireMock() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);
        WireMock.reset();
    }

    @DisplayName("Should successfully fetch, map, and sync a valid member from the API")
    @Test
    @Transactional
    void shouldSuccessfullySyncMemberAttributes() throws Exception {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        EmployeeAttributes attr1 = new EmployeeAttributes("Member 1",
                                                          "UpdatedLastName",
                                                          "NM1",
                                                          LocalDate.of(1990, 10, 1),
                                                          true,
                                                          "OrganisationUnit 1");
        EmployeeData emp1 = new EmployeeData(1L, "employee", attr1);

        PuzzleTimeResponseDto page1Response = new PuzzleTimeResponseDto(List.of(emp1), null);

        PuzzleTimeResponseDto emptyPageResponse = new PuzzleTimeResponseDto(List.of(), null);

        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page1Response))));

        stubFor(get(urlEqualTo("/api/employees?page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(emptyPageResponse))));

        syncJob.syncMemberAttributes();

        Member result = memberBusinessService.getById(1L);

        assertThat(result.getPtimeId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Member 1");
        assertThat(result.getLastName()).isEqualTo("UpdatedLastName");
        assertThat(result.getAbbreviation()).isEqualTo("NM1");
        assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1990, 10, 1));
        assertThat(result.getEmploymentState()).isEqualTo(EmploymentState.MEMBER);
        assertThat(result.getSyncErrorCount()).isZero();

        assertThat(result.getOrganisationUnit())
                .isNotNull()
                .extracting(OrganisationUnit::getName)
                .isEqualTo("OrganisationUnit 1");

        assertThat(result.getLastSuccessfulSync()).isNotNull().isAfter(LocalDateTime.now().minusMinutes(5));
    }

    @DisplayName("Should correctly process multiple pages until receiving an empty array")
    @Test
    void shouldHandleMultiplePaginationPages() throws Exception {
        EmployeeAttributes attr1 = new EmployeeAttributes("Member 1",
                                                          "UpdatedOne",
                                                          "M1",
                                                          LocalDate.of(1990, 1, 1),
                                                          true,
                                                          "OrganisationUnit 1");
        EmployeeAttributes attr2 = new EmployeeAttributes("Member 2",
                                                          "UpdatedTwo",
                                                          "M2",
                                                          LocalDate.of(1995, 5, 5),
                                                          true,
                                                          "OrganisationUnit 2");

        PuzzleTimeResponseDto page1 = new PuzzleTimeResponseDto(List.of(new EmployeeData(1L, "employee", attr1)), null);
        PuzzleTimeResponseDto page2 = new PuzzleTimeResponseDto(List.of(new EmployeeData(2L, "employee", attr2)), null);
        PuzzleTimeResponseDto page3 = new PuzzleTimeResponseDto(List.of(), null); // Stoppt die Schleife

        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page1))));

        stubFor(get(urlEqualTo("/api/employees?page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page2))));

        stubFor(get(urlEqualTo("/api/employees?page=3"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page3))));

        syncJob.syncMemberAttributes();

        Member result1 = memberBusinessService.getById(1L);
        assertThat(result1.getLastName()).isEqualTo("UpdatedOne");

        Member result2 = memberBusinessService.getById(2L);
        assertThat(result2.getLastName()).isEqualTo("UpdatedTwo");

        verify(1, getRequestedFor(urlEqualTo("/api/employees?page=3")));
    }

    @Test
    @DisplayName("Should successfully map JSON and ignore unknown additional attributes")
    void shouldIgnoreUnknownAttributesInJson() {
        String rawJsonWithExtraFields = """
                {
                  "data": [
                    {
                      "id": 1,
                      "type": "employee",
                      "some_weird_meta_tag": "ignore_me",
                      "attributes": {
                        "firstname": "Member 1",
                        "lastname": "Resilient",
                        "shortname": "M1",
                        "birthday": "1990-10-01",
                        "is_employed": true,
                        "department_name": "OrganisationUnit 1",
                        "favorite_color": "blue",
                        "shoe_size": 42
                      }
                    }
                  ]
                }
                """;

        String emptyPageJson = """
                {
                  "data": []
                }
                """;

        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(rawJsonWithExtraFields)));

        stubFor(get(urlEqualTo("/api/employees?page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(emptyPageJson)));

        syncJob.syncMemberAttributes();

        Member result = memberBusinessService.getById(1L);

        assertThat(result.getLastName()).isEqualTo("Resilient");
        assertThat(result.getSyncErrorCount()).isZero();
    }

    @DisplayName("Should ignore unknown API users without failing the sync")
    @Test
    void shouldIgnoreUnknownUserAndContinue() throws Exception {
        EmployeeAttributes unknownAttr = new EmployeeAttributes("Ghost",
                                                                "User",
                                                                "GHO",
                                                                LocalDate.of(1999, 1, 1),
                                                                true,
                                                                "OrganisationUnit 1");
        EmployeeData unknownEmp = new EmployeeData(9999L, "employee", unknownAttr);

        PuzzleTimeResponseDto page1 = new PuzzleTimeResponseDto(List.of(unknownEmp), null);
        PuzzleTimeResponseDto page2 = new PuzzleTimeResponseDto(List.of(), null);

        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page1))));

        stubFor(get(urlEqualTo("/api/employees?page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page2))));

        syncJob.syncMemberAttributes();

        assertThat(memberBusinessService.findByPtimeId(9999L)).isEmpty();
        assertThat(memberBusinessService.findByAbbreviation("GHO")).isEmpty();
    }

    @Test
    @DisplayName("Should increment syncErrorCount when API returns faulty data (e.g. missing firstname)")
    void shouldIncrementErrorCountOnFaultyApiData() throws Exception {
        EmployeeAttributes faultyAttr = new EmployeeAttributes("",
                                                               "UpdatedLastName",
                                                               "M1",
                                                               LocalDate.of(1990, 10, 1),
                                                               true,
                                                               "OrganisationUnit 1");
        EmployeeData faultyEmp = new EmployeeData(1L, "employee", faultyAttr);

        PuzzleTimeResponseDto page1 = new PuzzleTimeResponseDto(List.of(faultyEmp), null);
        PuzzleTimeResponseDto emptyPage = new PuzzleTimeResponseDto(List.of(), null);

        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page1))));

        stubFor(get(urlEqualTo("/api/employees?page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(emptyPage))));

        syncJob.syncMemberAttributes();

        Member result = memberBusinessService.getById(1L);

        assertThat(result.getLastName()).isNotEqualTo("UpdatedLastName");

        assertThat(result.getSyncErrorCount()).isEqualTo(1);
    }

    @DisplayName("Should abort pagination and log error when API returns HTTP 500")
    @Test
    void shouldStopAndLogErrorWhenApiReturnsHttpError() {
        stubFor(get(urlEqualTo("/api/employees?page=1"))
                .willReturn(aResponse().withStatus(500).withBody("Internal Server Error from PuzzleTime API")));

        syncJob.syncMemberAttributes();

        verify(1, getRequestedFor(urlEqualTo("/api/employees?page=1")));
        verify(0, getRequestedFor(urlEqualTo("/api/employees?page=2")));
    }
}
