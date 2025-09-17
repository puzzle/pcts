package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.organisation_unit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.OrganisationUnitMapper;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
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
@RequestMapping("/api/v1/organisation_unit")
public class OrganisationUnitController {
    private final OrganisationUnitMapper mapper;
    private final OrganisationUnitBusinessService service;

    @Autowired
    public OrganisationUnitController(OrganisationUnitMapper mapper, OrganisationUnitBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all organisation units")
    @ApiResponse(responseCode = "200", description = "A list of organisation units", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrganisationUnitDto.class))) })
    @GetMapping
    public ResponseEntity<List<OrganisationUnitDto>> getOrganisationUnit() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get an organisation unit by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A single organisation unit", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) }),
            @ApiResponse(responseCode = "404", description = "OrganisationUnit not found", content = @Content) })
    @GetMapping("{id}")
    public ResponseEntity<OrganisationUnitDto> getOrganisationUnitById(@PathVariable long id) {
        OrganisationUnit organisationUnit = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(organisationUnit));
    }

    @Operation(summary = "Create a new organisation unit")
    @ApiResponse(responseCode = "201", description = "organisation unit created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) })
    @PostMapping
    public ResponseEntity<OrganisationUnitDto> createNew(@Valid @RequestBody OrganisationUnitDto dto) {
        OrganisationUnit newOrganisationUnit = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newOrganisationUnit));
    }

    @Operation(summary = "Update an organisation unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organisation unit updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Organisation unit not found", content = @Content) })
    @PutMapping("{id}")
    public ResponseEntity<OrganisationUnitDto> updateOrganisationUnit(@PathVariable Long id,
                                                                      @RequestBody OrganisationUnitDto dto) {
        OrganisationUnit updatedOrganisationUnit = service.update(id, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedOrganisationUnit));
    }

    @Operation(summary = "Delete an organisation unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organisation unit deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organisation unit not found", content = @Content) })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrganisationUnit(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
