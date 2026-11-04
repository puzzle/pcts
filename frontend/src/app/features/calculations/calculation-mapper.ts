import { toDegreeCalculationModel, toDegreeCalculationDto } from './degree-calculation/degree-calculation-mapper';
import { CalculationModel, CalculationDto } from './calculationDto';

export function toCalculationModel(dto: CalculationDto): CalculationModel {
  return {
    ...dto,
    degreeCalculations: dto.degreeCalculations.map((degreeDto) => toDegreeCalculationModel(degreeDto))
  };
}

export function toCalculationDto(model: CalculationModel): CalculationDto {
  return {
    ...model,
    degreeCalculations: model.degreeCalculations.map(toDegreeCalculationDto)
  };
}
