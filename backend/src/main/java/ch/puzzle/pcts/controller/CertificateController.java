package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/certificates")
public class CertificateController {
    private final CertificateBusinessService service;
    private final CertificateMapper mapper;

    public CertificateController(CertificateBusinessService service, CertificateMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all Certificates")
    @ApiResponse(responseCode = "200", description = "A list off Certificates", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertificateDto.class))) })
    @GetMapping
    public ResponseEntity<List<CertificateDto>> getCertificate() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get Certificate by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A single certificate", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Certificate not found", content = @Content) })
    @GetMapping("{id}")
    public ResponseEntity<CertificateDto> getCertificatesById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Operation(summary = "Create a new Certificate")
    @ApiResponse(responseCode = "201", description = "Certificate created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @PostMapping
    public ResponseEntity<CertificateDto> createNew(@RequestBody CertificateDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.create(mapper.fromDto(dto))));
    }

    @Operation(summary = "Update a Certificate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificate updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Certificate not found", content = @Content) })
    @PutMapping("{id}")
    public ResponseEntity<CertificateDto> updateCertificate(@PathVariable Long id, @RequestBody CertificateDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.fromDto(dto))));
    }

    @Operation(summary = "Delete a Certificate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Certificate deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Certificate not found", content = @Content) })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
