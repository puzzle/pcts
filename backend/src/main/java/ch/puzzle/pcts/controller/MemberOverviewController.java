package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.mapper.MemberOverviewMapper;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import ch.puzzle.pcts.service.business.MemberOverviewBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member-overviews")
@Tag(name = "member-overviews", description = "Get the member and everything associated with the member")
public class MemberOverviewController {
    private final MemberOverviewMapper memberOverviewMapper;
    private final MemberOverviewBusinessService memberOverviewBusinessService;

    public MemberOverviewController(MemberOverviewMapper mapper, MemberOverviewBusinessService service) {
        this.memberOverviewMapper = mapper;
        this.memberOverviewBusinessService = service;
    }

    @Operation(summary = "Get the member-overview by memberId")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the member-overview.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MemberOverviewDto.class)) })
    @GetMapping("{memberId}")
    public ResponseEntity<MemberOverviewDto> getMemberOverviewByMemberId(@Parameter(description = "The ID of the member whose overview should be retrieved.", required = true)
    @PathVariable Long memberId) {
        List<MemberOverview> memberOverviews = memberOverviewBusinessService.getById(memberId);
        return ResponseEntity.ok(memberOverviewMapper.toDto(memberOverviews));
    }
}
