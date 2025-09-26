package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.mapper.DegreeTypeMapper;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/degree-types")
@Tag(name = "degree-types")
public class DegreeTypeController {
    private final DegreeTypeMapper mapper;
    private final DegreeTypeBusinessService service;

    @Autowired
    public DegreeTypeController(DegreeTypeMapper mapper, DegreeTypeBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all degree types")
    @ApiResponse(responseCode = "200", description = "A list of degree types.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DegreeTypeDto.class))) })
    @GetMapping
    public ResponseEntity<List<DegreeTypeDto>> getDegreeType() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Create a degree type")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The degree-type object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Degree type created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) })
    @PostMapping
    public ResponseEntity<DegreeTypeDto> createNew(@Valid @RequestBody DegreeTypeDto dto) {
        DegreeType newDegreeType = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newDegreeType));
    }

    @Operation(summary = "Get a single degree type by ID")
    @ApiResponse(responseCode = "200", description = "The requested degree type.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) })
    @GetMapping("{id}")
    public ResponseEntity<DegreeTypeDto> getById(@Parameter(description = "ID of the degree type to retrieve.")
    @PathVariable Long id) {
        DegreeType degreeType = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(degreeType));
    }

    @Operation(summary = "Update a degree type")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The degree-type object to be updated.", required = true)
    @ApiResponse(responseCode = "200", description = "Degree type updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) })
    @PutMapping("{id}")
    public ResponseEntity<DegreeTypeDto> update(@Parameter(description = "ID of the degree type to update.")
    @PathVariable() Long id, @RequestBody DegreeTypeDto dto) {
        DegreeType updatedDegreeType = service.update(id, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedDegreeType));
    }

    @Operation(summary = "Delete a degree type")
    @ApiResponse(responseCode = "204", description = "Degree type deleted successfully.", content = @Content)
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the degree type to delete.")
    @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
