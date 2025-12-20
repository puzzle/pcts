package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.RolePointDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.mapper.CalculationMapper;
import ch.puzzle.pcts.mapper.MemberMapper;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.security.annotation.IsAdmin;
import ch.puzzle.pcts.security.annotation.IsAdminOrOwner;
import ch.puzzle.pcts.security.annotation.IsAuthenticated;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@IsAdmin
@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "members", description = "Manage the members of the organisation, including status and organisation unit")
public class MemberController {
    private final MemberMapper mapper;
    private final CalculationMapper calculationMapper;
    private final MemberBusinessService service;

    public MemberController(MemberMapper mapper, CalculationMapper calculationMapper, MemberBusinessService service) {
        this.mapper = mapper;
        this.calculationMapper = calculationMapper;
        this.service = service;
    }

    @Operation(summary = "List all members")
    @ApiResponse(responseCode = "200", description = "A list of members.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MemberDto.class))) })
    @GetMapping
    public ResponseEntity<List<MemberDto>> getMember() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get a member by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the member.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) })
    @GetMapping("{memberId}")
    @IsAdminOrOwner
    public ResponseEntity<MemberDto> getMemberById(@Parameter(description = "ID of the member to retrieve.", required = true)
    @PathVariable @P("id") Long memberId) {
        Member member = service.getById(memberId);
        return ResponseEntity.ok(mapper.toDto(member));
    }

    @Operation(summary = "Get calculations for a member, optionally filtered by role")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the calculations.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CalculationDto.class))))
    @GetMapping("{memberId}/calculations")
    public ResponseEntity<List<CalculationDto>> getMemberCalculations(@Parameter(description = "ID of the member.", required = true)
    @PathVariable Long memberId, @Parameter(description = "Optional role ID to filter calculations.") @RequestParam(name = "roleId", required = false) Long roleId) {
        List<Calculation> calculations = service.getAllCalculationsByMemberIdAndRoleId(memberId, roleId);
        return ResponseEntity.ok(calculationMapper.toDto(calculations));
    }

    @Operation(summary = "Get roles and points for a member")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the roles and points.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RolePointDto.class))))
    @GetMapping("{memberId}/role-points")
    public ResponseEntity<List<RolePointDto>> getPointsForActiveCalculationsForRoleByMemberId(@Parameter(description = "ID of the member.", required = true)
    @PathVariable Long memberId) {
        List<Calculation> calculationList = service.getAllActiveCalculationsByMemberId(memberId);
        return ResponseEntity.ok(calculationMapper.toRolePointDto(calculationList));
    }

    @Operation(summary = "Create a new member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The member object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "member created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) })
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberInputDto dto) {
        Member newMember = service.create(mapper.fromDto(dto));
        return ResponseEntity.status(201).body(mapper.toDto(newMember));
    }

    @Operation(summary = "Update a member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated member data.", required = true)
    @ApiResponse(responseCode = "200", description = "member updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberInputDto.class)) })
    @PutMapping("{memberId}")
    public ResponseEntity<MemberDto> updateMember(@Parameter(description = "ID of the member to update.", required = true)
    @PathVariable Long memberId, @RequestBody MemberInputDto dto) {
        Member updatedMember = service.update(memberId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedMember));
    }

    @Operation(summary = "Delete a member")
    @ApiResponse(responseCode = "204", description = "member deleted successfully", content = @Content)
    @DeleteMapping("{memberId}")
    public ResponseEntity<Void> deleteMember(@Parameter(description = "ID of the member to delete.", required = true)
    @PathVariable Long memberId) {
        service.delete(memberId);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Get the currently logged in member")
    @ApiResponse(responseCode = "200", description = "The currently logged in member", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) })
    @GetMapping("myself")
    @IsAuthenticated
    public ResponseEntity<MemberDto> getMyself() {
        Member member = service.getLoggedInMember();
        return ResponseEntity.ok(mapper.toDto(member));
    }
}
