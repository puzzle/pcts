import { Relevancy } from '../relevancy.enum';
import { ExperienceModel } from '../../experiences/experience.model';

export interface ExperienceCalculationModel {
  id: number;
  experience: ExperienceModel;
  relevancy: Relevancy;
  comment: string | null;
}
