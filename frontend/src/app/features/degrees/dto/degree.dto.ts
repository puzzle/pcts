export interface DegreeDto {
  name: string;
  memberId: number;
  typeId: number;
  institution: string | null;
  completed: boolean;
  comment: string | null;
  startDate: string | null;
  endDate: string | null;
}
