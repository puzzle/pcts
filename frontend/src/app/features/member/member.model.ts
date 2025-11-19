import { OrganisationUnitModel } from '../organisation-unit/organisation-unit.model';
import { EmploymentState } from '../../shared/enum/employment-state.enum';
import { DateTime } from 'luxon';

export interface MemberModel {
  id: number;
  firstName: string;
  lastName: string;
  birthDate: DateTime;
  abbreviation: string | null;
  employmentState: EmploymentState;
  organisationUnit: OrganisationUnitModel;
  dateOfHire: DateTime | null;
}
