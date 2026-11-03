import { Relevancy } from '../relevancy.enum';
import { DegreeModel } from '../../degrees/degree.model';

export interface DegreeCalculationModel {
  id: number;
  degree: DegreeModel;
  relevancies: Map<Relevancy, number>;
  comment: string | null;
}
