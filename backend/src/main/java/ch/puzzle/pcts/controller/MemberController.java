package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.mapper.MemberMapper;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "member", description = "members of the organisation")
public class MemberController {
    private final MemberMapper mapper;
    private final MemberBusinessService service;

    @Autowired
    public MemberController(MemberMapper mapper, MemberBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Operation(summary = "List all members")
    @ApiResponse(responseCode = "200", description = "A list of members.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MemberDto.class))) })
    @GetMapping
    public ResponseEntity<List<MemberDto>> getMember() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get an member by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the member.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) })
    @GetMapping("{memberId}")
    public ResponseEntity<MemberDto> getMemberById(@Parameter(description = "ID of the member to retrieve.", required = true)
    @PathVariable Long memberId) {
        Member member = service.getById(memberId);
        return ResponseEntity.ok(mapper.toDto(member));
    }

    @Operation(summary = "Create a new member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The member object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "member created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberInputDto.class)) })
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@Valid @RequestBody MemberInputDto dto) {
        Member newMember = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newMember));
    }

    @Operation(summary = "Update an member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated member data.", required = true)
    @ApiResponse(responseCode = "200", description = "member updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberInputDto.class)) })
    @PutMapping("{memberId}")
    public ResponseEntity<MemberDto> updateMember(@Parameter(description = "ID of the member to update.", required = true)
    @PathVariable Long memberId, @RequestBody MemberInputDto dto) {
        Member updatedMember = service.update(memberId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedMember));
    }

    @Operation(summary = "Delete an member")
    @ApiResponse(responseCode = "204", description = "member deleted successfully", content = @Content)
    @DeleteMapping("{memberId}")
    public ResponseEntity<Void> deleteMember(@Parameter(description = "ID of the member to delete.", required = true)
    @PathVariable Long memberId) {
        service.delete(memberId);
        return ResponseEntity.status(204).build();
    }
}
