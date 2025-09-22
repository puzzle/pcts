package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeDto;
import ch.puzzle.pcts.dto.degreeType.DegreeTypeNameDto;
import ch.puzzle.pcts.dto.degreeType.DegreeTypeWithoutIdDto;
import ch.puzzle.pcts.mapper.DegreeTypeMapper;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
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
    @ApiResponse(responseCode = "200", description = "A list of degree types", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DegreeTypeDto.class))) })
    @GetMapping
    public ResponseEntity<List<DegreeTypeDto>> getDegreeType() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Create a degree type")
    @ApiResponse(responseCode = "201", description = "Degree type created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) })
    @PostMapping
    public ResponseEntity<DegreeTypeDto> createNew(@Valid @RequestBody DegreeTypeWithoutIdDto dto) {
        DegreeType newDegreeType = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newDegreeType));
    }

    @Operation(summary = "List all names and IDs of valid degree types")
    @ApiResponse(responseCode = "200", description = "A list of all valid degree types IDs and names", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DegreeTypeNameDto.class))) })
    @GetMapping("/names")
    public ResponseEntity<List<DegreeTypeNameDto>> getNameAndId() {
        return ResponseEntity.ok(service.getAllNames());
    }

    @Operation(summary = "Get a singel degree type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The requested degree type", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Degree type not found", content = @Content) })
    @GetMapping("{degreeTypeId}")
    public ResponseEntity<DegreeTypeDto> getById(@Parameter(description = "ID of the degree type to retrieve.")
    @PathVariable Long degreeTypeId) {
        DegreeType degreeType = service.getById(degreeTypeId);
        return ResponseEntity.ok(mapper.toDto(degreeType));
    }

    @Operation(summary = "Update a degree type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Degree type updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DegreeTypeDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Degree type not found", content = @Content) })
    @PutMapping("{degreeTypeId}")
    public ResponseEntity<DegreeTypeDto> update(@Parameter(description = "ID of the degree type to update")
    @PathVariable() Long degreeTypeId, @RequestBody DegreeTypeWithoutIdDto dto) {
        DegreeType updatedDegreeType = service.update(degreeTypeId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedDegreeType));
    }

    @Operation(summary = "Delete a degree type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Degree type deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Degree type not found", content = @Content) })
    @DeleteMapping("{degreeTypeId}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID of the degree type to delete")
    @PathVariable Long degreeTypeId) {
        service.delete(degreeTypeId);
        return ResponseEntity.status(204).build();
    }
}
