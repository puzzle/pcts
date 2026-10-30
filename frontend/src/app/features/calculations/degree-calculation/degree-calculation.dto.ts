import {DegreeModel} from '../../degrees/degree.model';
import {Relevancy} from '../relevancy.enum';

export interface DegreeCalculationDto {
  id: number;
  degree: DegreeModel;
  relevancies: Partial<Record<Relevancy, number>>;
  comment: string | null;
}
