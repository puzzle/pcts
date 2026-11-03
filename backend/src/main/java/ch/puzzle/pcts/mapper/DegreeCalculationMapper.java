package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.service.business.DegreeBusinessService;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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

    public DegreeCalculationDto toDto(DegreeCalculation degreeCalculation) {
        return new DegreeCalculationDto(degreeCalculation.getId(),
                                        degreeMapper.toDto(degreeCalculation.getDegree()),
                                        fillMissingWithZero(degreeCalculation.getRelevancies()),
                                        degreeCalculation.getComment());
    }

    public DegreeCalculation fromDto(DegreeCalculationInputDto dto) {
        return DegreeCalculation.Builder
                .builder()
                .withCalculation(null)
                .withDegree(degreeBusinessService.getById(dto.degreeId()))
                .withComment(dto.comment())
                .withRelevancy(Map.of(dto.relevancy(), dto.weight()))
                .build();
    }

    public Map<Relevancy, BigDecimal> fillMissingWithZero(Map<Relevancy, BigDecimal> inputMap) {
        Map<Relevancy, BigDecimal> resultMap = new EnumMap<>(Relevancy.class);

        resultMap.putAll(inputMap);

        if (resultMap.size() == 3) {
            return resultMap;
        }

        for (Relevancy relevancy : Relevancy.values()) {
            if (!relevancy.equals(Relevancy.NONE)) {
                resultMap.putIfAbsent(relevancy, BigDecimal.ZERO);
            }
        }

        return resultMap;
    }
}
