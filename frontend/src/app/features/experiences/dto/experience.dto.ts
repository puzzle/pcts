export interface ExperienceDto {
  name: string;
  memberId: number;
  experienceTypeId: number;
  comment: string | null;
  employer: string | null;
  percent: number;
  startDate: string | null;
  endDate: string | null;
}
