package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceUpdateDto;
import ch.puzzle.pcts.mapper.LeadershipExperiencesMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.LeadershipExperiencesBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leadership-experiences")
@Tag(name = "leadership experience", description = "Manage the leadership experience of members which are associated with a member")
public class LeadershipExperienceController {

    private final LeadershipExperiencesBusinessService businessService;
    private final LeadershipExperiencesMapper mapper;

    public LeadershipExperienceController(LeadershipExperiencesBusinessService businessService,
                                          LeadershipExperiencesMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a leadership experience by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the leadership experience.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) })
    @GetMapping("{leadershipExperienceId}")
    public ResponseEntity<LeadershipExperienceDto> getLeadershipExperience(@Parameter(description = "ID of the leadership experience to retrieve.", required = true)
    @PathVariable Long leadershipExperienceId) {
        Certificate certificate = businessService.getById(leadershipExperienceId);
        return ResponseEntity.ok(mapper.toDto(certificate));
    }

    @Operation(summary = "Create a new leadership experience")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The leadership experience object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Leadership experience created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) })
    @PostMapping
    public ResponseEntity<LeadershipExperienceDto> createLeadershipExperience(@RequestBody LeadershipExperienceInputDto dto) {
        Certificate certificate = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(certificate));
    }

    @Operation(summary = "Update a leadership experience")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated leadership experience data.", required = true)
    @ApiResponse(responseCode = "200", description = "Leadership experience updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) })
    @PutMapping("{leadershipExperienceId}")
    public ResponseEntity<LeadershipExperienceDto> updateLeadershipExperience(@Parameter(description = "ID of the leadership experience to update.", required = true)
    @PathVariable Long leadershipExperienceId, @RequestBody LeadershipExperienceUpdateDto dto) {
        Certificate certificate = businessService.update(leadershipExperienceId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(certificate));
    }

    @Operation(summary = "Delete a leadership experience")
    @ApiResponse(responseCode = "204", description = "Leadership experience deleted successfully.", content = @Content)
    @DeleteMapping("{leadershipExperienceId}")
    public ResponseEntity<Void> deleteLeadershipExperience(@Parameter(description = "ID of the leadership experience to delete.", required = true)
    @PathVariable Long leadershipExperienceId) {
        businessService.delete(leadershipExperienceId);
        return ResponseEntity.status(204).build();
    }
}