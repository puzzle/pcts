import { DegreeOverviewModel } from './degree-overview.model';
import { ExperienceOverviewModel } from './experience-overview.model';
import { CertificateOverviewModel } from './certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './leadership-experience-overview.model';

export interface CvOverviewModel {
  degrees: DegreeOverviewModel[];
  experiences: ExperienceOverviewModel[];
  certificates: CertificateOverviewModel[];
  leadershipExperiences: LeadershipExperienceOverviewModel[];
}
