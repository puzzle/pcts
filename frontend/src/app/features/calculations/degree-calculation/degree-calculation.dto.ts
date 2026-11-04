import { Relevancy } from '../relevancy.enum';
import { DegreeModel } from '../../degrees/degree.model';

export interface DegreeCalculationDto {
  id: number;
  degree: DegreeModel;
  relevancies: Partial<Record<Relevancy, number>>;
  comment: string | null;
}

export type DegreeCalculationModel = Omit<DegreeCalculationDto, 'relevancies'> & {
  relevancies: Record<Relevancy, { weight: number;
    checked: boolean; }>;
};
