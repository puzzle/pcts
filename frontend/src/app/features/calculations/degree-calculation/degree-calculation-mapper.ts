import { Relevancy } from '../relevancy.enum';
import { DegreeCalculationModel } from './degree-calculation.model';
import { DegreeCalculationDto } from './degree-calculation-model';

export function toDegreeCalculationDto(dto: DegreeCalculationModel): DegreeCalculationDto {
  const mappedRelevancies = Object.entries(dto.relevancies)
    .reduce((acc, [key,
      value]) => {
      const weight = value ?? 0;

      acc[key as Relevancy] = {
        weight: weight,
        checked: weight > 0
      };

      return acc;
    }, {} as Record<Relevancy, { weight: number;
      checked: boolean; }>);

  return {
    id: dto.id,
    degree: dto.degree,
    comment: dto.comment,
    relevancies: mappedRelevancies
  };
}

export function toDegreeCalculationModel(model: DegreeCalculationDto): DegreeCalculationModel {
  const mappedRelevancies = Object.entries(model.relevancies)
    .reduce((acc, [key,
      data]) => {
      acc[key as Relevancy] = data.weight;
      return acc;
    }, {} as Partial<Record<Relevancy, number>>);

  return {
    id: model.id,
    degree: model.degree,
    comment: model.comment,
    relevancies: mappedRelevancies
  };
}
