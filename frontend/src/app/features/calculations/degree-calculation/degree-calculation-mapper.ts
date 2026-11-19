import { Relevancy } from '../relevancy.enum';
import { DegreeCalculationDto, DegreeCalculationModel } from './degree-calculation.dto';
import { RelevancyType } from '../../../shared/generic-table/generic-checkbox-form-table/RelevancyWeightsRow';

export function toDegreeCalculationModel(dto: DegreeCalculationDto, index: number): DegreeCalculationModel {
  // we need a unique identifier for degrees. Therefor we use negative id that is made from the index
  return {
    id: dto.id ?? -index,
    degree: dto.degree,
    comment: dto.comment ?? '',
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

  // we need a unique identifier for degrees. Therefor we use negative id that is made from the index. We need to map back to the real ids.
  const dtoId = model.id ? mapModelIdToDtoID(model.id) : null;

  return {
    id: dtoId,
    degree: model.degree,
    comment: model.comment,
    relevancies: mappedRelevancies
  };
}

function mapModelIdToDtoID(id: number) {
  return id < 0 ? null : id;
}

function fillMissingWithZero(input: Partial<Record<Relevancy, number>> | null) {
  const result: Record<Relevancy, RelevancyType> = {} as Record<Relevancy, RelevancyType>;

  input = input ?? {};

  for (const relevancy of Object.values(Relevancy)) {
    result[relevancy] = {
      weight: input[relevancy] ?? 0,
      checked: (input[relevancy] ?? 0) > 0
    };
  }

  return result;
}
