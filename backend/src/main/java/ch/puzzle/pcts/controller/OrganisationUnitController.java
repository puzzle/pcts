package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.organisation_unit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.OrganisationUnitMapper;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organisation-units")
@Tag(name = "organisation-units")
public class OrganisationUnitController {
    private final OrganisationUnitMapper mapper;
    private final OrganisationUnitBusinessService service;

    @Autowired
    public OrganisationUnitController(OrganisationUnitMapper mapper, OrganisationUnitBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all organisation units")
    @ApiResponse(responseCode = "200", description = "A list of organisation units.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrganisationUnitDto.class))) })
    @GetMapping
    public ResponseEntity<List<OrganisationUnitDto>> getOrganisationUnit() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get an organisation unit by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the organisation unit.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) })
    @GetMapping("{organisationUnitId}")
    public ResponseEntity<OrganisationUnitDto> getOrganisationUnitById(@Parameter(description = "ID of the organisation unit to retrieve.", required = true)
    @PathVariable Long organisationUnitId) {
        OrganisationUnit organisationUnit = service.getById(organisationUnitId);
        return ResponseEntity.ok(mapper.toDto(organisationUnit));
    }

    @Operation(summary = "Create a new organisation unit")
    @RequestBody(description = "The organisation unit object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Organisation unit created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) })
    @PostMapping
    public ResponseEntity<OrganisationUnitDto> createOrganisationUnit(@Valid @RequestBody OrganisationUnitDto dto) {
        OrganisationUnit newOrganisationUnit = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newOrganisationUnit));
    }

    @Operation(summary = "Update an organisation unit")
    @RequestBody(description = "The updated organisation unit data.", required = true)
    @ApiResponse(responseCode = "200", description = "Organisation unit updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrganisationUnitDto.class)) })
    @PutMapping("{organisationUnitId}")
    public ResponseEntity<OrganisationUnitDto> updateOrganisationUnit(@Parameter(description = "ID of the organisation unit to update.", required = true)
    @PathVariable Long organisationUnitId, @RequestBody OrganisationUnitDto dto) {
        OrganisationUnit updatedOrganisationUnit = service.update(organisationUnitId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedOrganisationUnit));
    }

    @Operation(summary = "Delete an organisation unit")
    @ApiResponse(responseCode = "204", description = "Organisation unit deleted successfully", content = @Content)
    @DeleteMapping("{organisationUnitId}")
    public ResponseEntity<Void> deleteOrganisationUnit(@Parameter(description = "ID of the organisation unit to delete.", required = true)
    @PathVariable Long organisationUnitId) {
        service.delete(organisationUnitId);
        return ResponseEntity.status(204).build();
    }
}
