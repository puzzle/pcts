package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.mapper.CertificateTypeMapper;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.security.annotation.IsAdmin;
import ch.puzzle.pcts.security.annotation.IsAuthenticated;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@IsAdmin
@RestController
@RequestMapping("/api/v1/certificate-types")
@Tag(name = "certificate-types", description = "Manage professional certificate types and qualifications")
public class CertificateTypeController {
    private final CertificateTypeBusinessService service;
    private final CertificateTypeMapper mapper;

    public CertificateTypeController(CertificateTypeBusinessService service, CertificateTypeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all Certificate types")
    @ApiResponse(responseCode = "200", description = "A list off certificate types.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertificateTypeDto.class))) })
    @IsAuthenticated
    @GetMapping
    public ResponseEntity<List<CertificateTypeDto>> getCertificateTypes() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get Certificate type by ID")
    @ApiResponse(responseCode = "200", description = "A single certificate type.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateTypeDto.class)) })
    @IsAuthenticated
    @GetMapping("{certificateTypeId}")
    public ResponseEntity<CertificateTypeDto> getCertificateTypeById(@PathVariable Long certificateTypeId) {
        return ResponseEntity.ok(mapper.toDto(service.getById(certificateTypeId)));
    }

    @Operation(summary = "Create a new Certificate type")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The data to create a new certificate type.", required = true)
    @ApiResponse(responseCode = "201", description = "Certificate type created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateTypeDto.class)) })
    @PostMapping
    public ResponseEntity<CertificateTypeDto> createNewCertificateType(@RequestBody CertificateTypeDto dto) {
        CertificateType certificatetype = mapper.fromDto(dto);
        CertificateType created = service.create(certificatetype);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
    }

    @Operation(summary = "Update a Certificate type by ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Certificate type as json to update an existing Certificate type.", required = true)
    @ApiResponse(responseCode = "200", description = "Certificate type updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateTypeDto.class)) })
    @PutMapping("{certificateTypeId}")
    public ResponseEntity<CertificateTypeDto> updateCertificateType(@PathVariable Long certificateTypeId,
                                                                    @RequestBody CertificateTypeDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(certificateTypeId, mapper.fromDto(dto))));
    }

    @Operation(summary = "Delete a Certificate type by ID")
    @ApiResponse(responseCode = "204", description = "Certificate type deleted successfully.", content = @Content)
    @DeleteMapping("{certificateTypeId}")
    public ResponseEntity<Void> deleteCertificateType(@PathVariable Long certificateTypeId) {
        service.delete(certificateTypeId);
        return ResponseEntity.status(204).build();
    }
}
