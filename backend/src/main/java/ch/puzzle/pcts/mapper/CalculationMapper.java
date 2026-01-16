package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CalculationMapper {

    private final MemberMapper memberMapper;
    private final MemberBusinessService memberBusinessService;
    private final RoleMapper roleMapper;
    private final RoleBusinessService roleBusinessService;

    public CalculationMapper(MemberMapper memberMapper, RoleMapper roleMapper,
                             MemberBusinessService memberBusinessService, RoleBusinessService roleBusinessService) {

        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.roleMapper = roleMapper;
        this.roleBusinessService = roleBusinessService;
    }

    public List<CalculationDto> toDto(List<Calculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Calculation> fromDto(List<CalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public CalculationDto toDto(Calculation model) {
        return new CalculationDto(model.getId(),
                                  memberMapper.toDto(model.getMember()),
                                  roleMapper.toDto(model.getRole()),
                                  model.getState(),
                                  model.getPublicationDate(),
                                  model.getPublicizedBy());
    }

    public Calculation fromDto(CalculationInputDto dto) {
        return Calculation.Builder
                .builder()
                .withMember(this.memberBusinessService.getById(dto.memberId()))
                .withRole(this.roleBusinessService.getById(dto.roleId()))
                .withState(dto.state())
                .build();
    }
}
