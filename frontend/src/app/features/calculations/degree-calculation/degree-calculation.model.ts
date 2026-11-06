import { DegreeModel } from '../../degrees/degree.model';
import { Relevancy } from '../relevancy.enum';

export interface DegreeCalculationModel {
  id: number;
  degree: DegreeModel;
  relevancies: Map<Relevancy, number>;
  comment: string | null;
}
