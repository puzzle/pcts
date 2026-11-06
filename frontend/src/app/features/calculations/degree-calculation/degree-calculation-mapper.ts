import { Relevancy } from '../relevancy.enum';
import { DegreeCalculationDto, DegreeCalculationModel } from './degree-calculation.dto';

export function toDegreeCalculationModel(dto: DegreeCalculationDto): DegreeCalculationModel {
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

export function toDegreeCalculationDto(model: DegreeCalculationModel): DegreeCalculationDto {
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
    relevancies: fillMissingWithZero(mappedRelevancies as Map<Relevancy, number>) as Partial<Record<Relevancy, number>>
  };
}

function fillMissingWithZero(inputMap: Map<Relevancy, number>) {
  const resultMap = new Map<Relevancy, number>(inputMap);

  if (resultMap.size === 3) {
    return resultMap;
  }

  for (const relevancy of Object.values(Relevancy)) {
    if (relevancy !== Relevancy.NONE) {
      if (!resultMap.has(relevancy)) {
        resultMap.set(relevancy, 0);
      }
    }
  }

  return resultMap;
}
