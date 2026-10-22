import { Relevancy } from '../../relevancy.enum';

export interface DegreeCalculationInputDto {
  id: number;
  degreeId: number;
  relevancies: Map<Relevancy, number>;
  comment: string | null;
}
