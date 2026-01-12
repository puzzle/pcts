export interface ExperienceOverviewModel {
  id: number;
  name: string;
  employer: string;
  experienceTypeName: string;
  comment: string | null;
  startDate: Date;
  endDate: Date;
}
