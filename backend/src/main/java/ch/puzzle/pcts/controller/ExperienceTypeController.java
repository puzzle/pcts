package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.experienceType.ExperienceTypeDto;
import ch.puzzle.pcts.mapper.ExperienceTypeMapper;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/experience-types")
public class ExperienceTypeController {
    private final ExperienceTypeMapper mapper;
    private final ExperienceTypeBusinessService service;

    @Autowired
    public ExperienceTypeController(ExperienceTypeMapper mapper, ExperienceTypeBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all experience-types")
    @ApiResponse(responseCode = "200", description = "A list of experience-types", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExperienceTypeDto.class))) })
    @GetMapping
    public ResponseEntity<List<ExperienceTypeDto>> getExperienceType() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get a experience-type by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A single experience-type", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Experience-type not found", content = @Content) })
    @GetMapping("{id}")
    public ResponseEntity<ExperienceTypeDto> getExperienceTypeById(@PathVariable long id) {
        ExperienceType experienceType = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(experienceType));
    }

    @Operation(summary = "Create a new experience-type")
    @ApiResponse(responseCode = "201", description = "ExperienceType created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) })
    @PostMapping
    public ResponseEntity<ExperienceTypeDto> createNew(@Valid @RequestBody ExperienceTypeDto dto) {
        ExperienceType newExperienceType = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newExperienceType));
    }

    @Operation(summary = "Update a experience-type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ExperienceType updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceTypeDto.class)) }),
            @ApiResponse(responseCode = "404", description = "ExperienceType not found", content = @Content) })
    @PutMapping("{id}")
    public ResponseEntity<ExperienceTypeDto> updateExperienceType(@PathVariable Long id,
                                                                  @RequestBody ExperienceTypeDto dto) {
        ExperienceType updatedExperienceType = service.update(id, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedExperienceType));
    }

    @Operation(summary = "Delete a experience-type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "ExperienceType deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "ExperienceType not found", content = @Content) })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteExperienceType(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
