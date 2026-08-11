import { OrganisationUnitModel } from '../organisation-unit/organisation-unit.model';
import { EmploymentState } from '../../shared/enum/employment-state.enum';
import { RoleModel } from '../roles/RoleModel';

export interface MemberModel {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: Date;
  abbreviation: string | null;
  employmentState: EmploymentState;
  roles: RoleModel[];
  organisationUnit: OrganisationUnitModel;
  dateOfHire: Date | null;
}
