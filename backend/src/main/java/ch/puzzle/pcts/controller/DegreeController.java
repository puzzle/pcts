package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.mapper.DegreeMapper;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.security.annotation.IsAdmin;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@IsAdmin
@RestController
@RequestMapping("/api/v1/degrees")
@Tag(name = "degrees", description = "Manage the degrees which are associated with any one member")
public class DegreeController {
    DegreeBusinessService businessService;
    DegreeMapper mapper;

    DegreeController(DegreeBusinessService businessService, DegreeMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "Create a degree which is associated with a member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The degree object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Degree created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeDto.class)) })
    @PostMapping
    public ResponseEntity<DegreeDto> createNew(@Valid @RequestBody DegreeInputDto dto) {
        Degree newDegree = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newDegree));
    }

    @Operation(summary = "Get a single degree by ID")
    @ApiResponse(responseCode = "200", description = "The requested degree.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeDto.class)) })
    @GetMapping("{degreeId}")
    public ResponseEntity<DegreeDto> getById(@Parameter(description = "ID of the degree to retrieve.")
    @PathVariable Long degreeId) {
        Degree degree = businessService.getById(degreeId);
        return ResponseEntity.ok(mapper.toDto(degree));
    }

    @Operation(summary = "Update a degree")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The degree object to be updated.", required = true)
    @ApiResponse(responseCode = "200", description = "Degree updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeDto.class)) })
    @PutMapping("{degreeId}")
    public ResponseEntity<DegreeDto> update(@Parameter(description = "ID of the degree to update.")
    @PathVariable() Long degreeId, @RequestBody DegreeInputDto dto) {
        Degree updatedDegree = businessService.update(degreeId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedDegree));
    }

    @Operation(summary = "Delete a degree")
    @ApiResponse(responseCode = "204", description = "Degree deleted successfully.", content = @Content)
    @DeleteMapping("{degreeId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the degree to delete.")
    @PathVariable Long degreeId) {
        businessService.delete(degreeId);
        return ResponseEntity.status(204).build();
    }
}
