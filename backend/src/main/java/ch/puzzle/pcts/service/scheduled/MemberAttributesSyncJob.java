package ch.puzzle.pcts.service.scheduled;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.puzzletime.EmployeeAttributes;
import ch.puzzle.pcts.dto.puzzletime.EmployeeData;
import ch.puzzle.pcts.dto.puzzletime.PuzzleTimeResponseDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    private final OrganisationUnitBusinessService organisationUnitBusinessService;

    public MemberAttributesSyncJob(MemberBusinessService memberBusinessService,
                                   OrganisationUnitBusinessService organisationUnitBusinessService,
                                   @Value("${app.member-sync.enabled:false}") boolean enabled,
                                   @Value("${app.member-sync.url}") String apiUrl,
                                   @Value("${app.member-sync.username}") String username,
                                   @Value("${app.member-sync.password}") String password) {

        this.objectMapper = new ObjectMapper();
        this.memberBusinessService = memberBusinessService;
        this.organisationUnitBusinessService = organisationUnitBusinessService;
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
                log.info("API record with id {} has no existing attributes. Skipping.", apiPtimeId);
                continue;
            }

            String abbreviation = apiEmployee.attributes().shortname();
            Member member;
            boolean matchedViaAbbreviation = false;

            try {
                member = memberBusinessService.findByPtimeId(apiPtimeId);
            } catch (PCTSException _) {
                try {
                    member = memberBusinessService.findByAbbreviation(abbreviation);
                    matchedViaAbbreviation = true;
                    log
                            .info("Member {} found using abbreviation. ptime_id {} will be added.",
                                  abbreviation,
                                  apiPtimeId);
                } catch (PCTSException _) {
                    log
                            .warn("API record ignored: user with ptime_id {} and abbreviation {} was not found.",
                                  apiPtimeId,
                                  abbreviation);
                    continue;
                }
            }

            if (matchedViaAbbreviation) {
                member.setPtimeId(apiPtimeId);
            }

            try {
                validateAndUpdateMemberData(member, apiEmployee.attributes());

                member.setLastSuccessfulSync(LocalDateTime.now());
                member.setSyncErrorCount(0);

                memberBusinessService.update(member.getId(), member);
                log.debug("Member {} successfully synced.", member.getId());

            } catch (PCTSException e) {
                log
                        .warn("Processing failed for member {} (ptime_id {}): {}",
                              member.getId(),
                              apiPtimeId,
                              e.getMessage());
                incrementErrorCountAndSave(member, apiPtimeId);
            }
        }
    }

    private void validateAndUpdateMemberData(Member member, EmployeeAttributes attributes) throws PCTSException {

        if (attributes.firstname() == null || attributes.firstname().isBlank() || attributes.lastname() == null
            || attributes.lastname().isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_NULL,
                                                                    Map.of(FieldKey.FIELD, "firstname & lastname"))));
        }

        member.setFirstName(attributes.firstname());
        member.setLastName(attributes.lastname());

        if (attributes.birthday() != null && !attributes.birthday().isBlank()) {
            try {
                LocalDate parsedDate = LocalDate.parse(attributes.birthday());
                member.setBirthDate(parsedDate);
            } catch (DateTimeParseException _) {
                throw new PCTSException(HttpStatus.BAD_REQUEST,
                                        List
                                                .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_DATE,
                                                                        Map.of(FieldKey.FIELD, "birthDate"))));
            }
        }

        if (attributes.isEmployed()) {
            member.setEmploymentState(EmploymentState.MEMBER);
        } else {
            member.setEmploymentState(EmploymentState.EX_MEMBER);
        }

        String departmentName = attributes.departmentName();
        if (departmentName != null && !departmentName.isBlank()) {
            OrganisationUnit ou;
            try {
                ou = organisationUnitBusinessService.findByName(departmentName);
            } catch (PCTSException _) {
                log.info("OrganisationUnit '{}' does not exist in PCTS. Creating new instance.", departmentName);
                OrganisationUnit newOu = new OrganisationUnit();
                newOu.setName(departmentName);
                ou = organisationUnitBusinessService.create(newOu);
            }
            member.setOrganisationUnit(ou);
        } else {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_NULL,
                                                                    Map.of(FieldKey.FIELD, "organisationUnit"))));
        }
    }

    private void incrementErrorCountAndSave(Member dirtyMember, Long apiPtimeId) {
        try {
            Member cleanMember = memberBusinessService.findByPtimeId(apiPtimeId);

            int currentErrors = cleanMember.getSyncErrorCount() != null ? cleanMember.getSyncErrorCount() : 0;
            cleanMember.setSyncErrorCount(currentErrors + 1);

            memberBusinessService.update(cleanMember.getId(), cleanMember);
            log
                    .info("Error count for member {} successfully incremented to {}.",
                          cleanMember.getId(),
                          currentErrors + 1);

        } catch (PCTSException fallbackEx) {
            log
                    .error("Failed to save error count for member {} during fallback: {}",
                           dirtyMember.getId(),
                           fallbackEx.getMessage());
        }
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