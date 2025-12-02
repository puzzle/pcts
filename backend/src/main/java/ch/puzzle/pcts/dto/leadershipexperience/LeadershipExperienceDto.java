package ch.puzzle.pcts.dto.leadershipexperience;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record LeadershipExperienceDto(
        @Schema(description = "The unique identifier of the leadership experience.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,

        @Schema(description = "The member associated with this leadership experience.", exampleClasses = MemberDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberDto member,

        @Schema(description = "The type of leadership awarded to the member.", exampleClasses = CertificateTypeDto.class, requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) LeadershipExperienceTypeDto experience,

        @Schema(description = "An optional comment for the leadership experience.", example = "Completed via fast-track program", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String comment) {
}
