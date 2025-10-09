package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
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

@RestController
@RequestMapping("api/v1/certificates")
@Tag(name = "certificates", description = "certificates of members")
public class CertificateController {
    private final CertificateBusinessService service;
    private final CertificateMapper mapper;

    public CertificateController(CertificateBusinessService service, CertificateMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "List all Certificates")
    @ApiResponse(responseCode = "200", description = "A list off Certificates.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CertificateDto.class))) })
    @GetMapping
    public ResponseEntity<List<CertificateDto>> getCertificates() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get Certificate by ID")
    @ApiResponse(responseCode = "200", description = "A single certificate.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @GetMapping("{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Operation(summary = "Create a new Certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The data to create a new Certificate.", required = true)
    @ApiResponse(responseCode = "201", description = "Certificate created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @PostMapping
    public ResponseEntity<CertificateDto> createNewCertificate(@RequestBody CertificateDto dto) {
        Certificate certificate = mapper.fromDto(dto);
        Certificate created = service.create(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(created));
    }

    @Operation(summary = "Update a Certificate by ID")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Certificate as json to update an existing Certificate.", required = true)
    @ApiResponse(responseCode = "200", description = "Certificate updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CertificateDto.class)) })
    @PutMapping("{id}")
    public ResponseEntity<CertificateDto> updateCertificate(@PathVariable Long id, @RequestBody CertificateDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, mapper.fromDto(dto))));
    }

    @Operation(summary = "Delete a Certificate by ID")
    @ApiResponse(responseCode = "204", description = "Certificate deleted successfully.", content = @Content)
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(204).build();
    }
}
