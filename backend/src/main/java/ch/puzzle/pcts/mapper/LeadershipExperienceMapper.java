package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceMapper {
    private final LeadershipExperienceTypeMapper leadershipExperienceTypeMapper;
    private final MemberMapper memberMapper;
    private final LeadershipExperienceTypeBusinessService leadershipExperienceTypeBusinessService;
    private final MemberBusinessService memberBusinessService;

    public LeadershipExperienceMapper(LeadershipExperienceTypeMapper leadershipExperienceTypeMapper,
                                      MemberMapper memberMapper,
                                      LeadershipExperienceTypeBusinessService leadershipExperiencesBusinessService,
                                      MemberBusinessService memberBusinessService) {
        this.leadershipExperienceTypeMapper = leadershipExperienceTypeMapper;
        this.memberMapper = memberMapper;
        this.leadershipExperienceTypeBusinessService = leadershipExperiencesBusinessService;
        this.memberBusinessService = memberBusinessService;
    }

    public List<LeadershipExperienceDto> toDto(List<LeadershipExperience> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<LeadershipExperience> fromDto(List<LeadershipExperienceInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public LeadershipExperienceDto toDto(LeadershipExperience model) {
        return new LeadershipExperienceDto(model.getId(),
                                           memberMapper.toDto(model.getMember()),
                                           leadershipExperienceTypeMapper.toDto(model.getLeadershipExperienceType()),
                                           model.getComment());
    }

    public LeadershipExperience fromDto(LeadershipExperienceInputDto dto) {
        return LeadershipExperience.Builder
                .builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withLeadershipExperienceType(leadershipExperienceTypeBusinessService
                        .getById(dto.leadershipExperienceTypeId()))
                .withComment(dto.comment())
                .build();
    }
}
