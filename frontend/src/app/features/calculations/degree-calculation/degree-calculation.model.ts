import { DegreeModel } from '../../degrees/degree.model';
import { Relevancy } from '../relevancy.enum';

export interface DegreeCalculationModel {
  id: number;
  degree: DegreeModel;
  weight: number;
  relevancy: Relevancy;
  comment: string | null;
}
