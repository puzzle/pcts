import { OrganisationUnitModel } from '../organisation-unit/organisation-unit.model';
import { EmploymentState } from '../../shared/enum/employment-state.enum';

export interface MemberModel {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: Date;
  abbreviation: string;
  employmentState: EmploymentState;
  organisationUnit: OrganisationUnitModel;
  dateOfHire: Date;
}

