import { MemberModel } from '../../features/member/member.model';
import { EmploymentState } from '../enum/employment-state.enum';
import { OrganisationUnitModel } from '../../features/organisation-unit/organisation-unit.model';
import { MemberDto } from '../../features/member/dto/member.dto';
import { DateTime } from 'luxon';
import { removeTimeZone } from '../utils/DateHandler';

export const organisationUnit1: OrganisationUnitModel = {
  id: 1,
  name: '/mem'
};

export const organisationUnit2: OrganisationUnitModel = {
  id: 2,
  name: '/sys'
};

export const organisationUnit3: OrganisationUnitModel = {
  id: 3,
  name: '/bbt'
};

export const organisationUnit4: OrganisationUnitModel = {
  id: 4,
  name: '/rookie'
};

export const member1: MemberModel = {
  id: 1,
  firstName: 'Lena',
  lastName: 'Müller',
  birthDate: DateTime.fromISO('2000-12-01'),
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: DateTime.fromISO('2018-12-01'),
  organisationUnit: organisationUnit1
};

export const member2: MemberModel = {
  id: 2,
  firstName: 'Sophie',
  lastName: 'Keller',
  birthDate: DateTime.fromISO('2000-12-01'),
  abbreviation: 'SK',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: DateTime.fromISO('2018-12-01'),
  organisationUnit: organisationUnit2
};

export const member3: MemberModel = {
  id: 3,
  firstName: 'Mara',
  lastName: 'Becker',
  birthDate: DateTime.fromISO('2000-12-01'),
  abbreviation: 'BD',
  employmentState: EmploymentState.EX_MEMBER,
  dateOfHire: DateTime.fromISO('2018-12-01'),
  organisationUnit: organisationUnit3
};

export const member4: MemberModel = {
  id: 4,
  firstName: 'John',
  lastName: 'Doe',
  birthDate: DateTime.fromISO('2000-12-01'),
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnit: organisationUnit4
};

export const memberDto1: MemberDto = {
  firstName: 'Lena',
  lastName: 'Müller',

  birthDate: removeTimeZone(new Date('2000-12-01')),
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: removeTimeZone(new Date('2018-12-01')),
  organisationUnitId: 1
};

export const memberDto2: MemberDto = {
  firstName: 'John',
  lastName: 'Doe',
  birthDate: removeTimeZone(new Date('2000-12-01')),
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnitId: 4
};
