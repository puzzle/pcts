import { CvOverviewModel } from './detail-view/cv/cv.model';
import { MemberOverviewModel } from './member-overview.model';

export interface MemberCvOverviewModel {
  member: MemberOverviewModel;
  cv: CvOverviewModel;
}
