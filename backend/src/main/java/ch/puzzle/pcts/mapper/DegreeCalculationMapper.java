package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DegreeCalculationMapper {

    private final DegreeBusinessService degreeBusinessService;
    private final DegreeMapper degreeMapper;

    public DegreeCalculationMapper(DegreeBusinessService degreeBusinessService, DegreeMapper degreeMapper) {
        this.degreeBusinessService = degreeBusinessService;
        this.degreeMapper = degreeMapper;
    }

    public List<DegreeCalculationDto> toDto(List<DegreeCalculation> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<DegreeCalculation> fromDto(List<DegreeCalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public DegreeCalculation fromDto(DegreeCalculationInputDto dto) {
        return new DegreeCalculation(dto.id(),
                                     null,
                                     degreeBusinessService.getById(dto.degreeId()),
                                     dto.relevancy(),
                                     dto.weight(),
                                     dto.comment());
    }

    public DegreeCalculationDto toDto(DegreeCalculation model) {
        return new DegreeCalculationDto(model.getId(),
                                        degreeMapper.toDto(model.getDegree()),
                                        model.getWeight(),
                                        model.getRelevancy(),
                                        model.getComment());
    }
}
