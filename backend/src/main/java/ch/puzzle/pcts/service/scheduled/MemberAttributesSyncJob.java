package ch.puzzle.pcts.service.scheduled;

import ch.puzzle.pcts.dto.puzzletime.EmployeeAttributes;
import ch.puzzle.pcts.dto.puzzletime.EmployeeData;
import ch.puzzle.pcts.dto.puzzletime.PuzzleTimeResponseDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemberAttributesSyncJob {

    private static final Logger log = LoggerFactory.getLogger(MemberAttributesSyncJob.class);

    private final boolean enabled;
    private final String apiUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MemberBusinessService memberBusinessService;

    public MemberAttributesSyncJob(MemberBusinessService memberBusinessService,
                                   @Value("${app.member-sync.enabled:false}") boolean enabled,
                                   @Value("${app.member-sync.url}") String apiUrl,
                                   @Value("${app.member-sync.username}") String username,
                                   @Value("${app.member-sync.password}") String password) {

        this.objectMapper = new ObjectMapper();
        this.memberBusinessService = memberBusinessService;
        this.enabled = enabled;
        this.apiUrl = apiUrl;

        this.httpClient = HttpClient.newBuilder().authenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        }).build();
    }

    @Scheduled(cron = "${app.member-sync.cron}")
    public void syncMemberAttributes() {
        if (!enabled) {
            log.info("Member sync is disabled; skipping execution.");
            return;
        }

        log.info("Starting MemberAttributesSyncJob...");
        int page = 1;

        while (true) {
            try {
                URI uri = new URI(apiUrl + "?page=" + page);

                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(uri)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    log
                            .error("HTTP request failed with status code {} on page {}. Response: {}",
                                   response.statusCode(),
                                   page,
                                   response.body());
                    break;
                }

                PuzzleTimeResponseDto apiData = parseJsonResponse(response.body());

                if (apiData == null || apiData.data() == null || apiData.data().isEmpty()) {
                    log.info("Empty data array received on page {}. Pagination finished.", page);
                    break;
                }

                processAndSaveMembers(apiData.data());

                log.info("Successfully fetched and processed page {}", page);
                page++;

            } catch (URISyntaxException e) {
                log.error("Invalid API URI constructed for page {}: {}", page, e.getMessage(), e);
                break;
            } catch (IOException e) {
                log.error("Network or I/O error while fetching page {}: {}", page, e.getMessage(), e);
                break;
            } catch (InterruptedException e) {
                log.error("Sync job was interrupted during execution on page {}", page);
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("MemberAttributesSyncJob finished.");
    }

    private void processAndSaveMembers(List<EmployeeData> apiEmployees) {
        for (EmployeeData apiEmployee : apiEmployees) {
            Long apiPtimeId = apiEmployee.id();

            if (apiEmployee.attributes() == null) {
                log.info("API record with id {} has no existing attributes. Skipping", apiPtimeId);
                continue;
            }

            String abbreviation = apiEmployee.attributes().shortname();
            Member member;

            try {
                member = memberBusinessService.findByPtimeId(apiPtimeId);

            } catch (PCTSException _) {

                try {
                    member = memberBusinessService.findByAbbreviation(abbreviation);

                    member.setPtimeId(apiPtimeId);
                    log.info("Member {} found using abbreviation. ptime_id {} was added.", abbreviation, apiPtimeId);

                } catch (PCTSException _) {
                    log
                            .warn("API record ignored: user with ptime_id {} and abbreviation {} was not found.",
                                  apiPtimeId,
                                  abbreviation);
                    continue;
                }
            }

            if (isValidApiData(apiEmployee)) {
                updateMemberStammdaten(member, apiEmployee.attributes());
                member.setLastSuccessfulSync(LocalDateTime.now());
                member.setSyncErrorCount(0);
                log.debug("Member {} erfolgreich synchronisiert.", member.getId());
            } else {
                int currentErrors = member.getSyncErrorCount() != null ? member.getSyncErrorCount() : 0;
                member.setSyncErrorCount(currentErrors + 1);
                log.warn("API-Daten-Validierung für Member {} fehlgeschlagen. Fehlerzähler erhöht.", member.getId());
            }

            try {
                memberBusinessService.update(member.getId(), member);
            } catch (PCTSException e) {
                log.error("BusinessService lehnte das Speichern von Member {} ab: {}", member.getId(), e.getMessage());

                try {
                    Member cleanMember = memberBusinessService.findByPtimeId(apiPtimeId);

                    int currentErrors = cleanMember.getSyncErrorCount() != null ? cleanMember.getSyncErrorCount() : 0;
                    cleanMember.setSyncErrorCount(currentErrors + 1);

                    memberBusinessService.update(cleanMember.getId(), cleanMember);
                } catch (Exception fallbackEx) {
                    log
                            .error("Konnte Fehlerzähler für Member {} im Fallback nicht speichern: {}",
                                   member.getId(),
                                   fallbackEx.getMessage());
                }
            }
        }
    }

    private boolean isValidApiData(EmployeeData apiEmployee) {
        return apiEmployee.attributes().firstname() != null && !apiEmployee.attributes().firstname().isBlank()
               && apiEmployee.attributes().lastname() != null && !apiEmployee.attributes().lastname().isBlank();
    }

    private void updateMemberStammdaten(Member member, EmployeeAttributes attributes) {
        member.setFirstName(attributes.firstname());
        member.setLastName(attributes.lastname());
    }

    private PuzzleTimeResponseDto parseJsonResponse(String jsonBody) throws JsonProcessingException {
        try {
            return objectMapper.readValue(jsonBody, PuzzleTimeResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            throw e;
        }
    }
}