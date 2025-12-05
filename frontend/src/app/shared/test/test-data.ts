import { MemberModel } from '../../features/member/member.model';
import { EmploymentState } from '../enum/employment-state.enum';
import { OrganisationUnitModel } from '../../features/organisation-unit/organisation-unit.model';
import { MemberDto } from '../../features/member/dto/member.dto';
import { TagModel } from '../../features/certificates/certificate-type/tag.model';
import { CertificateTypeModel } from '../../features/certificates/certificate-type/certificate-type.model';
import { CertificateKind } from '../../features/certificates/certificate-type/certificate-kind.enum';
import { CertificateModel } from '../../features/certificates/certificate.model';
import { CertificateDto } from '../../features/certificates/dto/certificate.dto';
import { ExperienceTypeModel } from '../../features/experiences/experience-type/experience-type.model';
import { ExperienceModel } from '../../features/experiences/experience.model';
import { ExperienceDto } from '../../features/experiences/dto/experience.dto';
import { LeadershipExperienceModel } from '../../features/leadership-experiences/leadership-experience.model';
import {
  LeadershipExperienceTypeModel
} from '../../features/leadership-experiences/leadership-experiences-type/leadership-experience-type.model';

export const url = '/api/v1/data';

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
  birthDate: new Date('2000-12-01'),
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit1
};

export const member2: MemberModel = {
  id: 2,
  firstName: 'Sophie',
  lastName: 'Keller',
  birthDate: new Date('2000-12-01'),
  abbreviation: 'SK',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit2
};

export const member3: MemberModel = {
  id: 3,
  firstName: 'Mara',
  lastName: 'Becker',
  birthDate: new Date('2000-12-01'),
  abbreviation: 'BD',
  employmentState: EmploymentState.EX_MEMBER,
  dateOfHire: new Date('2018-12-01'),
  organisationUnit: organisationUnit3
};

export const member4: MemberModel = {
  id: 4,
  firstName: 'John',
  lastName: 'Doe',
  birthDate: new Date('2000-12-01'),
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnit: organisationUnit4
};

export const memberDto1: MemberDto = {
  firstName: 'Lena',
  lastName: 'Müller',
  birthDate: '2000-12-01',
  abbreviation: 'LM',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: '2018-12-01',
  organisationUnitId: 1
};

export const memberDto2: MemberDto = {
  firstName: 'John',
  lastName: 'Doe',
  birthDate: '2000-12-01',
  abbreviation: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnitId: 4
};

export const tag1: TagModel = {
  id: 1,
  name: 'GitLab'
};

export const tag2: TagModel = {
  id: 2,
  name: 'AWS'
};

export const tag3: TagModel = {
  id: 3,
  name: 'Ruby'
};

export const tag4: TagModel = {
  id: 4,
  name: 'PHP'
};

export const certificateType1: CertificateTypeModel = {
  id: 1,
  name: 'GitLab & AWS Certificate',
  points: 10,
  comment: null,
  tags: [tag1,
    tag2],
  certificateKind: CertificateKind.CERTIFICATE
};

export const certificateType2: CertificateTypeModel = {
  id: 2,
  name: 'Ruby Certificate',
  points: 15,
  comment: null,
  tags: [tag3],
  certificateKind: CertificateKind.CERTIFICATE
};

export const certificate1: CertificateModel = {
  id: 1,
  member: member1,
  certificateType: certificateType1,
  completedAt: new Date('2022-03-15'),
  validUntil: new Date('2024-03-15'),
  comment: null
};

export const certificate2: CertificateModel = {
  id: 2,
  member: member2,
  certificateType: certificateType2,
  completedAt: new Date('2021-10-10'),
  validUntil: null,
  comment: 'Completed with distinction'
};

export const certificateDto1: CertificateDto = {
  memberId: 3,
  certificateTypeId: 3,
  completedAt: '2020-06-01',
  validUntil: '2023-01-01',
  comment: null
};

export const certificateDto2: CertificateDto = {
  memberId: 4,
  certificateTypeId: 4,
  completedAt: '2019-09-20',
  validUntil: '2024-09-20',
  comment: 'Leadership excellence'
};

export const experienceType1: ExperienceTypeModel = {
  id: 1,
  name: 'Internship',
  highlyRelevantPoints: 3,
  limitedRelevantPoints: 2,
  littleRelevantPoints: 1
};

export const experienceType2: ExperienceTypeModel = {
  id: 2,
  name: 'Work experience',
  highlyRelevantPoints: 5,
  limitedRelevantPoints: 3,
  littleRelevantPoints: 1
};


export const experience1: ExperienceModel = {
  id: 1,
  name: 'Java software developer',
  member: member1,
  experienceType: experienceType1,
  comment: null,
  employer: 'TechCorp AG',
  percent: 100,
  startDate: new Date('2019-02-01'),
  endDate: new Date('2022-02-01')
};

export const experience2: ExperienceModel = {
  id: 2,
  name: 'Team leader',
  member: member2,
  experienceType: experienceType2,
  comment: 'Managed several cross-functional teams',
  employer: 'InnovateX GmbH',
  percent: 80,
  startDate: new Date('2018-05-15'),
  endDate: null
};

export const experienceDto1: ExperienceDto = {
  name: 'Ruby developer',
  memberId: 3,
  experienceTypeId: 3,
  comment: 'Warehouse shift supervisor',
  employer: 'LogiTrans AG',
  percent: 60,
  startDate: '2016-11-01',
  endDate: '2019-11-01'
};

export const experienceDto2: ExperienceDto = {
  name: 'Secretary',
  memberId: 4,
  experienceTypeId: 4,
  comment: null,
  employer: 'City School District',
  percent: 50,
  startDate: '2020-01-10',
  endDate: null
};

export const leadershipExperienceType1: LeadershipExperienceTypeModel = {
  id: 1,
  name: 'Expert',
  points: 1,
  comment: 'J+S Expert',
  certificateKind: CertificateKind.YOUTH_AND_SPORT
};

export const leadershipExperienceType2: LeadershipExperienceTypeModel = {
  id: 2,
  name: 'Officer',
  points: 2,
  comment: null,
  certificateKind: CertificateKind.MILITARY_FUNCTION
};

export const leadershipExperience1: LeadershipExperienceModel = {
  id: 1,
  member: member1,
  leadershipExperienceType: leadershipExperienceType1,
  comment: 'J+S Expert and Leader'
};

export const leadershipExperience2: LeadershipExperienceModel = {
  id: 2,
  member: member2,
  leadershipExperienceType: leadershipExperienceType2,
  comment: null
};

