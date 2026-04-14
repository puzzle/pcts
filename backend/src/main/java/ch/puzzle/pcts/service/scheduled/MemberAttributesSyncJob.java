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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class MemberAttributesSyncJob {

    private static final Logger log = LoggerFactory.getLogger(MemberAttributesSyncJob.class);

    private final boolean enabled;
    private final RestClient restClient;
    private final MemberBusinessService memberBusinessService;
    private final OrganisationUnitBusinessService organisationUnitBusinessService;

    public MemberAttributesSyncJob(MemberBusinessService memberBusinessService,
                                   OrganisationUnitBusinessService organisationUnitBusinessService,
                                   @Value("${app.member-sync.enabled:false}") boolean enabled,
                                   @Value("${app.member-sync.url}") String apiUrl,
                                   @Value("${app.member-sync.username}") String username,
                                   @Value("${app.member-sync.password}") String password) {

        this.memberBusinessService = memberBusinessService;
        this.organisationUnitBusinessService = organisationUnitBusinessService;
        this.enabled = enabled;

        this.restClient = RestClient
                .builder()
                .baseUrl(apiUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .defaultHeader("Accept", "application/json")
                .build();
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
                PuzzleTimeResponseDto apiData = restClient
                        .get()
                        .uri("?page={page}", page)
                        .retrieve()
                        .body(PuzzleTimeResponseDto.class);

                if (apiData == null || apiData.data() == null || apiData.data().isEmpty()) {
                    log.info("Empty data array received on page {}. Pagination finished.", page);
                    break;
                }

                processAndSaveMembers(apiData.data());

                log.info("Successfully fetched and processed page {}", page);
                page++;

            } catch (RestClientResponseException e) {
                log
                        .error("HTTP request failed with status code {} on page {}. Response: {}",
                               e.getStatusCode(),
                               page,
                               e.getResponseBodyAsString());
                break;
            } catch (RestClientException e) {
                log.error("Network or mapping error while fetching page {}: {}", page, e.getMessage(), e);
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
            Optional<Member> memberOpt = memberBusinessService.findByPtimeId(apiPtimeId);

            if (memberOpt.isEmpty()) {
                memberOpt = memberBusinessService.findByAbbreviation(abbreviation);

                if (memberOpt.isEmpty()) {
                    log
                            .warn("API record ignored: user with ptime_id {} and abbreviation {} was not found.",
                                  apiPtimeId,
                                  abbreviation);
                    continue;
                }

                log.info("Member {} found using abbreviation. ptime_id {} will be added.", abbreviation, apiPtimeId);
            }

            Member member = memberOpt.get();

            Long ptimeIdToUpdate = (member.getPtimeId() == null) ? apiPtimeId : null;

            try {
                validateAndUpdateMemberData(member, apiEmployee.attributes());

                memberBusinessService.update(member.getId(), member);

                memberBusinessService.updateSyncMetadata(member.getId(), ptimeIdToUpdate, LocalDateTime.now(), 0);

                log.debug("Member {} successfully synced.", member.getId());

            } catch (PCTSException e) {
                log
                        .warn("Processing failed for member {} (ptime_id {}): {} - Errors: {}",
                              member.getId(),
                              apiPtimeId,
                              e.getMessage(),
                              e.getErrors());
                incrementErrorCountAndSave(member);
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

        if (attributes.birthday() != null) {
            member.setBirthDate(attributes.birthday());
        }

        if (attributes.isEmployed()) {
            member.setEmploymentState(EmploymentState.MEMBER);
        } else {
            member.setEmploymentState(EmploymentState.EX_MEMBER);
        }

        String departmentName = attributes.departmentName();
        if (departmentName != null && !departmentName.isBlank()) {

            member.setOrganisationUnit(findOrCreateOrganisationUnit(departmentName));

        } else {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List
                                            .of(new GenericErrorDto(ErrorKey.ATTRIBUTE_NOT_NULL,
                                                                    Map.of(FieldKey.FIELD, "organisationUnit"))));
        }
    }

    private OrganisationUnit findOrCreateOrganisationUnit(String departmentName) {
        Optional<OrganisationUnit> orgUnitOpt = organisationUnitBusinessService.findByName(departmentName);

        if (orgUnitOpt.isEmpty()) {
            log.info("OrganisationUnit '{}' does not exist in PCTS. Creating new instance.", departmentName);
            OrganisationUnit newOu = new OrganisationUnit();
            newOu.setName(departmentName);
            return organisationUnitBusinessService.create(newOu);
        }

        return orgUnitOpt.get();
    }

    private void incrementErrorCountAndSave(Member dirtyMember) {
        try {
            Member cleanMember = memberBusinessService.getById(dirtyMember.getId());

            int currentErrors = cleanMember.getSyncErrorCount() != null ? cleanMember.getSyncErrorCount() : 0;
            cleanMember.setSyncErrorCount(currentErrors + 1);

            memberBusinessService.updateSyncMetadata(cleanMember.getId(), null, null, cleanMember.getSyncErrorCount());
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
}