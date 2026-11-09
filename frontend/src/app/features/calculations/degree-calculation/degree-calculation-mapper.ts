import { Relevancy } from '../relevancy.enum';
import { DegreeCalculationDto, DegreeCalculationModel } from './degree-calculation.dto';
import { RelevancyType } from '../../../shared/generic-table/generic-checkbox-form-table/RelevancyWeightsRow';

export function toDegreeCalculationModel(dto: DegreeCalculationDto): DegreeCalculationModel {
  return {
    id: dto.id,
    degree: dto.degree,
    comment: dto.comment,
    relevancies: fillMissingWithZero(dto.relevancies)
  };
}

export function toDegreeCalculationDto(model: DegreeCalculationModel): DegreeCalculationDto {
  const mappedRelevancies = Object.entries(model.relevancies)
    .reduce((acc, [key,
      data]) => {
      if (data.checked) {
        acc[key as Relevancy] = data.weight;
      }
      return acc;
    }, {} as Partial<Record<Relevancy, number>>);

  return {
    id: model.id,
    degree: model.degree,
    comment: model.comment,
    relevancies: mappedRelevancies
  };
}

function fillMissingWithZero(input: Partial<Record<Relevancy, number>>) {
  const result: Record<Relevancy, RelevancyType> = {} as Record<Relevancy, RelevancyType>;

  for (const relevancy of Object.values(Relevancy)) {
    result[relevancy] = {
      weight: input[relevancy] ?? 0,
      checked: (input[relevancy] ?? 0) > 0
    };
  }

  return result;
}
