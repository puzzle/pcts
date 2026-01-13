export interface ExperienceOverviewModel {
  id: number;
  name: string;
  employer: string | null;
  experienceTypeName: string;
  comment: string | null;
  startDate: Date;
  endDate: Date | null;
}
