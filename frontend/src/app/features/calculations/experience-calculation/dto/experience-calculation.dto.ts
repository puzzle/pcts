import { Relevancy } from '../../relevancy.enum';

export interface ExperienceCalculationInputDto {
  id: number;
  experienceId: number;
  relevancy: Relevancy;
  comment: string | null;
}
