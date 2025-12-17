package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.security.annotation.IsAdmin;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@IsAdmin
@RestController
@RequestMapping("/api/v1/certificates")
@Tag(name = "certificates", description = "Manage the certificates of members which are associated with a member")
public class CertificateController {
    private final CertificateBusinessService businessService;
    private final CertificateMapper mapper;

    public CertificateController(CertificateBusinessService businessService, CertificateMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a certificate by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the certificates.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @GetMapping("{certificateId}")
    public ResponseEntity<CertificateDto> getCertificate(@Parameter(description = "ID of the certificate to retrieve.", required = true)
    @PathVariable Long certificateId) {
        Certificate certificate = businessService.getById(certificateId);
        return ResponseEntity.ok(mapper.toDto(certificate));
    }

    @Operation(summary = "Create a new certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The certificate object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Certificate created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateInputDto dto) {
        Certificate newCertificate = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newCertificate));
    }

    @Operation(summary = "Update a certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated certificate data.", required = true)
    @ApiResponse(responseCode = "200", description = "Certificate updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateInputDto.class)) })
    @PutMapping("{certificateId}")
    public ResponseEntity<CertificateDto> updateCertificate(@Parameter(description = "ID of the certificate to update.", required = true)
    @PathVariable Long certificateId, @RequestBody CertificateInputDto dto) {
        Certificate updatedCertificate = businessService.update(certificateId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedCertificate));
    }

    @Operation(summary = "Delete a certificate")
    @ApiResponse(responseCode = "204", description = "Certificate deleted successfully.", content = @Content)
    @DeleteMapping("{certificateId}")
    public ResponseEntity<Void> deleteCertificate(@Parameter(description = "ID of the certificate to delete.", required = true)
    @PathVariable Long certificateId) {
        businessService.delete(certificateId);
        return ResponseEntity.status(204).build();
    }
}
