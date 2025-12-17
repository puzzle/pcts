package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceTypeMapper;
import ch.puzzle.pcts.security.annotation.IsAdmin;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@IsAdmin
@RestController
@RequestMapping("/api/v1/leadership-experience-types")
@Tag(name = "leadership-experience-types", description = "Manage leadership experience types")
public class LeadershipExperienceTypeController {
    private final LeadershipExperienceTypeBusinessService service;
    private final LeadershipExperienceTypeMapper mapper;

    public LeadershipExperienceTypeController(LeadershipExperienceTypeBusinessService service,
                                              LeadershipExperienceTypeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all leadership-experience types")
    @ApiResponse(responseCode = "200", description = "A list off leadership-experience types.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeadershipExperienceTypeDto.class))) })
    @GetMapping
    public ResponseEntity<List<LeadershipExperienceTypeDto>> getLeadershipExperienceTypes() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get leadership-experience type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A single leadership-experience type.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceTypeDto.class)) }), })
    @GetMapping("{leadershipExperienceTypeId}")
    public ResponseEntity<LeadershipExperienceTypeDto> getLeadershipExperienceTypeById(@PathVariable Long leadershipExperienceTypeId) {
        return ResponseEntity.ok(mapper.toDto(service.getById(leadershipExperienceTypeId)));
    }

    @Operation(summary = "Create a new leadership-experience type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "leadership-experience type created successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceTypeDto.class)) }), })
    @PostMapping
    public ResponseEntity<LeadershipExperienceTypeDto> createNewLeadershipExperienceType(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The LeadershipExperience type as json to create a new LeadershipExperience.", required = true)
    @RequestBody LeadershipExperienceTypeDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.fromDto(dto))));
    }

    @Operation(summary = "Update a leadership-experience type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LeadershipExperience type updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceTypeDto.class)) }) })
    @PutMapping("{leadershipExperienceTypeId}")
    public ResponseEntity<LeadershipExperienceTypeDto> updateLeadershipExperienceType(@PathVariable Long leadershipExperienceTypeId,
                                                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The LeadershipExperience type as json to update an existing LeadershipExperience.", required = true)
                                                                                      @RequestBody LeadershipExperienceTypeDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(leadershipExperienceTypeId, mapper.fromDto(dto))));
    }

    @Operation(summary = "Delete a leadership-experience type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "LeadershipExperience type deleted successfully", content = @Content), })
    @DeleteMapping("{leadershipExperienceTypeId}")
    public ResponseEntity<Void> deleteLeadershipExperienceType(@PathVariable Long leadershipExperienceTypeId) {
        service.delete(leadershipExperienceTypeId);
        return ResponseEntity.status(204).build();
    }
}
