package ch.puzzle.pcts.dto.degree;

import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO representing a degree, including associated member and type information.")
public record DegreeDto(

        @Schema(description = "The unique identifier of the degree.", example = "1", accessMode = Schema.AccessMode.READ_ONLY) Long id,

        @Schema(description = "The member associated with this degree.", exampleClasses = MemberDto.class) MemberDto member,

        @Schema(description = "The name of the degree.", example = "Master of Computer Science") String name,

        @Schema(description = "The institution granting the degree.", example = "ETH Zürich") String institution,

        @Schema(description = "Indicates whether the degree has been completed.", example = "true") Boolean completed,

        @Schema(description = "The type of the degree", exampleClasses = DegreeTypeDto.class) DegreeTypeDto type,

        @Schema(description = "The start date of the degree program.", example = "2018-09-01") Date startDate,

        @Schema(description = "The end date of the degree program.", example = "2022-06-30") Date endDate,

        @Schema(description = "Additional comments or notes about the degree.", example = "Graduated summa cum laude.") String comment

) {
}
