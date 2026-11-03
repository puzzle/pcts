import { Relevancy } from '../relevancy.enum';
import { DegreeCalculationModel } from './degree-calculation.model';

export type DegreeCalculationDto = Omit<DegreeCalculationModel, 'relevancies'> & {
  relevancies: Record<Relevancy, { weight: number;
    checked: boolean; }>;
};
