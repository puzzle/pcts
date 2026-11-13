import { Relevancy } from '../../relevancy.enum';

export interface DegreeCalculationInputDto {
  id: number | null;
  degreeId: number;
  relevancies: Record<Relevancy, number>;
  comment: string | null;
}
