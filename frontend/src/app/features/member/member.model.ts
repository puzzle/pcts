import { OrganisationUnit } from '../organisation-unit/organisation-unit.model';

export interface Member {
  id: number;
  name: string;
  last_name: string;
  birthday: Date;
  nickname: string;
  employment_state: EmploymentState;
  date_of_hire: Date;
  is_admin: boolean;
  organisation_unit: OrganisationUnit;
}

