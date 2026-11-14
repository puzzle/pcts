import { Relevancy } from '../relevancy.enum';
import { DegreeModel } from '../../degrees/degree.model';

export interface DegreeCalculationDto {
  id: number | null;
  degree: DegreeModel;
  relevancies: Partial<Record<Relevancy, number>> | null;
  comment: string | null;
}

export type DegreeCalculationModel = Omit<DegreeCalculationDto, 'relevancies' | 'comment'> & {
  relevancies: Record<Relevancy, { weight: number;
    checked: boolean; }>;
  comment: string;

};
