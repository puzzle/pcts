export interface DegreeDto {
  name: string;
  memberId: number;
  degreeTypeId: number;
  institution: string;
  completed: boolean;
  comment: string | null;
  startDate: string | null;
  endDate: string | null;
}
