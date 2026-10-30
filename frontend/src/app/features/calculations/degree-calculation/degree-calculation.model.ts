import { Relevancy } from '../relevancy.enum';
import {DegreeCalculationDto} from './degree-calculation.dto';

export type DegreeCalculationModel = Omit<DegreeCalculationDto, 'relevancies'> & {
  relevancies: Record<Relevancy, number | null>; // null = not selected
};

function fillRelevancy(relevancies: Map<Relevancy, number>): Map<Relevancy, number | null> {
  return new Map<Relevancy, number | null>([
    [Relevancy.STRONGLY, relevancies.get(Relevancy.STRONGLY) || 0],
    [Relevancy.NORMAL, relevancies.get(Relevancy.NORMAL) || 0],
    [Relevancy.POORLY, relevancies.get(Relevancy.POORLY) || 0]
  ]);
}

export function toDegreeCalculationModel(dto: DegreeCalculationDto): DegreeCalculationModel {
  return {
    id: dto.id,
    degree: dto.degree,
    comment: dto.comment,
    relevancies: fillRelevancy(dto.relevancies)
  } as DegreeCalculationModel
}


// export function toDegreeCalculationDto(model: DegreeCalculationModel): DegreeCalculationDto { … }
