package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.mapper.ExperienceMapper;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/experiences")
@Tag(name = "experience", description = " Manage the experiences of members, including experience type")
public class ExperienceController {
    private final ExperienceMapper mapper;
    private final ExperienceBusinessService service;

    public ExperienceController(ExperienceMapper mapper, ExperienceBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all experiences")
    @ApiResponse(responseCode = "200", description = "A list of experiences.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExperienceDto.class))) })
    @GetMapping
    public ResponseEntity<List<ExperienceDto>> getExperiences() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get an experience by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A single experience.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDto.class)) }), })
    @GetMapping("{experienceId}")
    public ResponseEntity<ExperienceDto> getExperienceById(@Parameter(description = "ID of the experience to retrieve.", required = true)
    @PathVariable Long experienceId) {
        Experience experience = service.getById(experienceId);
        return ResponseEntity.ok(mapper.toDto(experience));
    }

    @Operation(summary = "Create a new experience")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The experience object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "experience created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDto.class)) })
    @PostMapping
    public ResponseEntity<ExperienceDto> createExperience(@Valid @RequestBody ExperienceInputDto dto) {
        Experience newExperience = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newExperience));
    }

    @Operation(summary = "Update an experience")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated experience data.", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "experience updated successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDto.class)) }), })
    @PutMapping("{experienceId}")
    public ResponseEntity<ExperienceDto> updateExperience(@Parameter(description = "ID of the experience to update.", required = true)
    @PathVariable Long experienceId, @RequestBody ExperienceInputDto dto) {
        Experience updatedExperience = service.update(experienceId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedExperience));
    }

    @Operation(summary = "Delete an experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "experience deleted successfully.", content = @Content) })
    @DeleteMapping("{experienceId}")
    public ResponseEntity<Void> deleteExperience(@Parameter(description = "ID of the experience to delete.", required = true)
    @PathVariable Long experienceId) {
        service.delete(experienceId);
        return ResponseEntity.status(204).build();
    }
}
