import { LeadershipExperienceTypeOverviewModel } from './Leadership-experience-type-overview.model';

export interface LeadershipExperienceOverviewModel {
  id: number;
  leadershipExperienceType: LeadershipExperienceTypeOverviewModel;
  comment: string | null;
}
