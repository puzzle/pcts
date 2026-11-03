import { toDegreeCalculationDto, toDegreeCalculationModel } from './degree-calculation/degree-calculation-mapper';
import { CalculationDto, CalculationModel } from './calculationModel';

export function toCalculationDto(dto: CalculationModel): CalculationDto {
  return {
    ...dto,
    degreeCalculations: dto.degreeCalculations.map((degreeDto) => toDegreeCalculationDto(degreeDto))
  };
}

export function toCalculationModel(model: CalculationDto): CalculationModel {
  return {
    ...model,
    degreeCalculations: model.degreeCalculations.map(toDegreeCalculationModel)
  };
}
