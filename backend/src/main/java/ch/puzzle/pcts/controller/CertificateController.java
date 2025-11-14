package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.mode.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificates")
@Tag(name = "member certificate", description = "Manage the certificates of members, including type and tag of the certificate")
public class CertificateController {
    private final CertificateBusinessService businessService;
    private final CertificateMapper mapper;

    public CertificateController(CertificateBusinessService businessService, CertificateMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "List all member certificates")
    @ApiResponse(responseCode = "200", description = "A list of member certificates.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertificateDto.class))) })
    @GetMapping
    public ResponseEntity<List<CertificateDto>> getCertificate() {
        return ResponseEntity.ok(mapper.toDto(businessService.getAll()));
    }

    @Operation(summary = "Get a member certificate by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the member certificates.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @GetMapping("{memberCertificateId}")
    public ResponseEntity<CertificateDto> getCertificate(@Parameter(description = "ID of the member certificate to retrieve.", required = true)
    @PathVariable Long memberCertificateId) {
        Certificate certificate = businessService.getById(memberCertificateId);
        return ResponseEntity.ok(mapper.toDto(certificate));
    }

    @Operation(summary = "Create a new member certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The member certificate object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "member certificate created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateInputDto dto) {
        Certificate newCertificate = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newCertificate));
    }

    @Operation(summary = "Update a member certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated member certificate data.", required = true)
    @ApiResponse(responseCode = "200", description = "member certificate updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateInputDto.class)) })
    @PutMapping("{memberCertificateId}")
    public ResponseEntity<CertificateDto> updateCertificate(@Parameter(description = "ID of the member to update.", required = true)
    @PathVariable Long memberCertificateId, @RequestBody CertificateInputDto dto) {
        Certificate updatedCertificate = businessService.update(memberCertificateId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedCertificate));
    }

    @Operation(summary = "Delete a member certificate")
    @ApiResponse(responseCode = "204", description = "member certificate deleted successfully", content = @Content)
    @DeleteMapping("{memberCertificateId}")
    public ResponseEntity<Void> deleteCertificate(@Parameter(description = "ID of the member certificate to delete.", required = true)
    @PathVariable Long memberCertificateId) {
        businessService.delete(memberCertificateId);
        return ResponseEntity.status(204).build();
    }

}
