export interface Member {
  id: number;
  name: string;
  lastName: string;
  birthday: Date;
  abbreviation: string;
  employmentState: EmploymentState;
  organisationUnit: string;
  dateOfHire: Date;
}

export enum EmploymentState {
  ACTIVE = 'Member',
  ALUMNI = 'Ex Member',
  APPLICANT = 'Bewerber'
}
