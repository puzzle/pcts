package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberOverviewMapper {

    private final ObjectMapper mapper;

    public MemberOverviewMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public MemberOverviewDto toDto(MemberOverview entity) {
        try {
            return mapper.readValue(entity.getOverview(), MemberOverviewDto.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to map overview JSON", e);
        }
    }
}
