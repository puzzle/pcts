import { OrganisationUnit } from './organisation-unit.model';

export interface Member {
  id: number;
  name: string;
  last_name: string;
  birthday: Date;
  abbreviation: string;
  employment_state: EmploymentState;
  date_of_hire: Date;
  organisation_unit: OrganisationUnit;
}

export enum EmploymentState {
  MEMBER = 'Member',
  EX_MEMBER = 'Ex Member',
  BEWERBER = 'Bewerber'
}
