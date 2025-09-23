export interface Member {
  id: number;
  name: string;
  last_name: string;
  birthday: Date;
  abbreviation: string;
  employment_state: EmploymentState;
  date_of_hire: Date;
  is_admin: boolean;
  organisation_unit: string;
}

export enum EmploymentState {
  MEMBER = 'Member',
  EX_MEMBER = 'Ex Member',
  BEWERBER = 'Bewerber'
}
