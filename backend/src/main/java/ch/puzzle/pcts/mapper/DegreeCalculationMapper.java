package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DegreeCalculationMapper {

    private final DegreeBusinessService degreeBusinessService;

    public DegreeCalculationMapper(DegreeBusinessService degreeBusinessService) {
        this.degreeBusinessService = degreeBusinessService;
    }

    public List<DegreeCalculation> fromDto(List<DegreeCalculationInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public DegreeCalculation fromDto(DegreeCalculationInputDto dto) {
        return new DegreeCalculation(null,
                                     null,
                                     degreeBusinessService.getById(dto.degreeId()),
                                     dto.relevancy(),
                                     dto.weight());
    }
}
