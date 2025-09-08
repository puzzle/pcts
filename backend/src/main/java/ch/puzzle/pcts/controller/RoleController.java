package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.mapper.RoleMapper;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleMapper mapper;
    private final RoleBusinessService service;

    @Autowired
    public RoleController(RoleMapper mapper, RoleBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all roles")
    @ApiResponse(responseCode = "200", description = "A list of roles", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RoleDto.class))) })
    @GetMapping
    public ResponseEntity<List<RoleDto>> getRole() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A single role", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @GetMapping("{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable long id) {
        Role role = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(role));
    }

    @Operation(summary = "Create a new role")
    @ApiResponse(responseCode = "201", description = "Role created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDto.class)) })
    @PostMapping
    public ResponseEntity<RoleDto> createNew(@Valid @RequestBody RoleDto dto) {
        Role newRole = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newRole));
    }

    @Operation(summary = "Update a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @PutMapping("{id}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto dto) {
        Role updatedRole = service.update(id, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedRole));
    }

    @Operation(summary = "Delete a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RoleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content) })
    @DeleteMapping("{id}")
    public ResponseEntity<RoleDto> deleteRole(@PathVariable Long id) {
        Role deletedRole = service.delete(id);
        return ResponseEntity.status(204).body(mapper.toDto(deletedRole));
    }
}
