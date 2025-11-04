package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.membercertificate.MemberCertificateDto;
import ch.puzzle.pcts.dto.membercertificate.MemberCertificateInputDto;
import ch.puzzle.pcts.mapper.MemberCertificateMapper;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.service.business.MemberCertificateBusinessService;
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
@RequestMapping("/api/v1/member_certificates")
@Tag(name = "member certificate", description = "Manage the certificates of members, including type and tag of the certificate")
public class MemberCertificateController {
    private final MemberCertificateBusinessService businessService;
    private final MemberCertificateMapper mapper;

    public MemberCertificateController(MemberCertificateBusinessService businessService,
                                       MemberCertificateMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "List all member certificates")
    @ApiResponse(responseCode = "200", description = "A list of member certificates.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MemberCertificateDto.class))) })
    @GetMapping
    public ResponseEntity<List<MemberCertificateDto>> getMemberCertificate() {
        return ResponseEntity.ok(mapper.toDto(businessService.getAll()));
    }

    @Operation(summary = "Get a member certificate by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the member certificates.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberCertificateDto.class)) })
    @GetMapping("{memberCertificateId}")
    public ResponseEntity<MemberCertificateDto> getMemberCertificateById(@Parameter(description = "ID of the member certificate to retrieve.", required = true)
    @PathVariable Long memberCertificateId) {
        MemberCertificate memberCertificate = businessService.getById(memberCertificateId);
        return ResponseEntity.ok(mapper.toDto(memberCertificate));
    }

    @Operation(summary = "Create a new member certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The member certificate object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "member certificate created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberCertificateDto.class)) })
    @PostMapping
    public ResponseEntity<MemberCertificateDto> createMemberCertificate(@RequestBody MemberCertificateInputDto dto) {
        MemberCertificate newMemberCertificate = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newMemberCertificate));
    }

    @Operation(summary = "Update a member certificate")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated member certificate data.", required = true)
    @ApiResponse(responseCode = "200", description = "member certificate updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberCertificateInputDto.class)) })
    @PutMapping("{memberCertificateId}")
    public ResponseEntity<MemberCertificateDto> updateMemberCertificate(@Parameter(description = "ID of the member to update.", required = true)
    @PathVariable Long memberCertificateId, @RequestBody MemberCertificateInputDto dto) {
        MemberCertificate updatedMemberCertificate = businessService.update(memberCertificateId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedMemberCertificate));
    }

    @Operation(summary = "Delete a member certificate")
    @ApiResponse(responseCode = "204", description = "member certificate deleted successfully", content = @Content)
    @DeleteMapping("{memberCertificateId}")
    public ResponseEntity<Void> deleteMemberCertificate(@Parameter(description = "ID of the member certificate to delete.", required = true)
    @PathVariable Long memberCertificateId) {
        businessService.delete(memberCertificateId);
        return ResponseEntity.status(204).build();
    }

}
