package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.experienceType.ExperienceTypeDto;
import ch.puzzle.pcts.mapper.ExperienceTypeMapper;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/experience-types")
@Tag(name = "experience-type")
public class ExperienceTypeController {
    private final ExperienceTypeMapper mapper;
    private final ExperienceTypeBusinessService service;

    @Autowired
    public ExperienceTypeController(ExperienceTypeMapper mapper, ExperienceTypeBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all experience-types")
    @ApiResponse(responseCode = "200", description = "A list of experience-types.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExperienceTypeDto.class))) })
    @GetMapping
    public ResponseEntity<List<ExperienceTypeDto>> getExperienceTypes() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get an experience-type by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A single experience-type.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) }), })
    @GetMapping("{experienceTypeId}")
    public ResponseEntity<ExperienceTypeDto> getExperienceTypeById(@Parameter(description = "ID of the experience-type to retrieve.", required = true)
    @PathVariable Long experienceTypeId) {
        ExperienceType experienceType = service.getById(experienceTypeId);
        return ResponseEntity.ok(mapper.toDto(experienceType));
    }

    @Operation(summary = "Create a new experience-type")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The experience-type object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "experience-type created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) })
    @PostMapping
    public ResponseEntity<ExperienceTypeDto> createExperienceType(@Valid @RequestBody ExperienceTypeDto dto) {
        ExperienceType newExperienceType = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newExperienceType));
    }

    @Operation(summary = "Update an experience-type")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated experience type data.", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "experience-type updated successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) }), })
    @PutMapping("{experienceTypeId}")
    public ResponseEntity<ExperienceTypeDto> updateExperienceType(@Parameter(description = "ID of the experience-type to update.", required = true)
    @PathVariable Long experienceTypeId, @RequestBody ExperienceTypeDto dto) {
        ExperienceType updatedExperienceType = service.update(experienceTypeId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedExperienceType));
    }

    @Operation(summary = "Delete an experience-type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "experience-type deleted successfully.", content = @Content) })
    @DeleteMapping("{experienceTypeId}")
    public ResponseEntity<Void> deleteExperienceType(@Parameter(description = "ID of the experience-type to delete.", required = true)
    @PathVariable Long experienceTypeId) {
        service.delete(experienceTypeId);
        return ResponseEntity.status(204).build();
    }
}
