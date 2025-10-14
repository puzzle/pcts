package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.leadership_experience.LeadershipExperienceDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceMapper;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
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

@RestController
@RequestMapping("api/v1/leadership-experiences")
@Tag(name = "leadership-experiences")
public class LeadershipExperienceController {
    private final LeadershipExperienceBusinessService service;
    private final LeadershipExperienceMapper mapper;

    public LeadershipExperienceController(LeadershipExperienceBusinessService service,
                                          LeadershipExperienceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all leadership-experiences")
    @ApiResponse(responseCode = "200", description = "A list off leadership-experiences.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LeadershipExperienceDto.class))) })
    @GetMapping
    public ResponseEntity<List<LeadershipExperienceDto>> getLeadershipExperiences() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get leadership-experience by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A single leadership-experience.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) }), })
    @GetMapping("{id}")
    public ResponseEntity<LeadershipExperienceDto> getLeadershipExperienceById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Operation(summary = "Create a new leadership-experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "leadership-experience created successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) }), })
    @PostMapping
    public ResponseEntity<LeadershipExperienceDto> createNewLeadershipExperience(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The LeadershipExperience as json to create a new LeadershipExperience.", required = true)
    @RequestBody LeadershipExperienceDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(mapper.fromDto(dto))));
    }

    @Operation(summary = "Update a LeadershipExperience by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LeadershipExperience updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = LeadershipExperienceDto.class)) }) })
    @PutMapping("{id}")
    public ResponseEntity<LeadershipExperienceDto> updateLeadershipExperience(@PathVariable Long id,
                                                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The LeadershipExperience as json to update an existing LeadershipExperience.", required = true)
                                                                              @RequestBody LeadershipExperienceDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.fromDto(dto))));
    }

    @Operation(summary = "Delete a LeadershipExperience by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "LeadershipExperience deleted successfully", content = @Content), })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLeadershipExperience(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
