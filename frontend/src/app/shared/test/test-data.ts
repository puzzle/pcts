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
import { DegreeTypeModel } from '../../features/degrees/degree-type/degree-type.model';
import { DegreeModel } from '../../features/degrees/degree.model';
import { DegreeOverviewModel } from '../../features/member/detail-view/cv/degree-overview.model';
import { MemberCvOverviewModel } from '../../features/member/member-cv-overview.model';
import { CalculationModel } from '../../features/calculations/calculation.model';
import { RoleModel } from '../../features/roles/role.model';
import { RolePointsModel } from '../../features/member/detail-view/RolePointsModel';
import { Relevancy } from '../../features/calculations/relevancy.enum';
import { CalculationState } from '../../features/calculations/calculation-state.enum';
import {
  CertificateCalculationModel
} from '../../features/calculations/certificate-calculation/certificate-calculation.model';
import {
  LeadershipExperienceCalculationModel
} from '../../features/calculations/leadership-experience-calculation/leadership-experience-calculation.model';
import { DegreeCalculationModel } from '../../features/calculations/degree-calculation/degree-calculation.model';
import {
  ExperienceCalculationModel
} from '../../features/calculations/experience-calculation/experience-calculation.model';

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
  email: 'muller@puzzle.ch',
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
  email: 'keller@puzzle.ch',
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
  email: 'becker@puzzle.ch',
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
  email: null,
  employmentState: EmploymentState.APPLICANT,
  dateOfHire: null,
  organisationUnit: organisationUnit4
};

export const memberDto1: MemberDto = {
  firstName: 'Lena',
  lastName: 'Müller',
  birthDate: '2000-12-01',
  abbreviation: 'LM',
  email: 'muller@puzzle.ch',
  employmentState: EmploymentState.MEMBER,
  dateOfHire: '2018-12-01',
  organisationUnitId: 1
};

export const memberDto2: MemberDto = {
  firstName: 'John',
  lastName: 'Doe',
  birthDate: '2000-12-01',
  abbreviation: null,
  email: null,
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
  experienceKind: CertificateKind.YOUTH_AND_SPORT
};

export const leadershipExperienceType2: LeadershipExperienceTypeModel = {
  id: 2,
  name: 'Officer',
  points: 2,
  comment: null,
  experienceKind: CertificateKind.MILITARY_FUNCTION
};

export const leadershipExperience1: LeadershipExperienceModel = {
  id: 1,
  member: member1,
  experience: leadershipExperienceType1,
  comment: 'J+S Expert and Leader'
};

export const leadershipExperience2: LeadershipExperienceModel = {
  id: 2,
  member: member2,
  experience: leadershipExperienceType2,
  comment: null
};


export const degreeType1: DegreeTypeModel = {
  id: 1,
  name: 'Bachelor',
  highlyRelevantPoints: 4,
  limitedRelevantPoints: 2,
  littleRelevantPoints: 1
};

export const degreeType2: DegreeTypeModel = {
  id: 2,
  name: 'Master',
  highlyRelevantPoints: 6,
  limitedRelevantPoints: 3,
  littleRelevantPoints: 1
};

export const degree1: DegreeModel = {
  id: 1,
  name: 'Bachelor of Computer Science',
  member: member1,
  type: degreeType1,
  institution: 'University of Applied Sciences Zurich',
  completed: true,
  comment: null,
  startDate: new Date('2015-09-01'),
  endDate: new Date('2018-06-30')
};

export const degree2: DegreeModel = {
  id: 2,
  name: 'Master of Business Administration',
  member: member2,
  type: degreeType2,
  institution: 'St. Gallen Business School',
  completed: false,
  comment: 'Thesis still in progress',
  startDate: new Date('2021-10-01'),
  endDate: null
};

export const memberOverview1: MemberCvOverviewModel = {
  member: {
    id: 1,
    firstName: 'Lena',
    lastName: 'Müller',
    employmentState: EmploymentState.MEMBER,
    abbreviation: 'LM',
    dateOfHire: new Date(),
    birthDate: new Date(),
    organisationUnitName: '/zh'
  },
  cv: {
    degrees: [{
      id: 1,
      name: 'Bachelor of Science in Mathematics',
      degreeTypeName: 'Bachelor\'s Degree',
      startDate: new Date(),
      endDate: new Date()
    }],
    experiences: [{
      id: 1,
      name: 'Software Engineer',
      employer: 'TechNova Solutions',
      experienceTypeName: 'Internship',
      comment: 'Worked on backend APIs and DevOps tasks.',
      startDate: new Date(),
      endDate: new Date()
    },
    {
      id: 3,
      name: 'Web Developer (Freelance)',
      employer: 'Freelance',
      experienceTypeName: 'Hackathon',
      comment: null,
      startDate: new Date(),
      endDate: new Date()
    }],
    certificates: [{
      id: 1,
      certificateTypeName: 'CompTIA A+',
      completedAt: new Date(),
      comment: 'Completed first aid training.'
    }],
    leadershipExperiences: [{
      id: 1,
      comment: 'LeadershipExperience',
      leadershipExperienceType: {
        name: 'Leadership Experience',
        certificateKind: CertificateKind.LEADERSHIP_TRAINING
      }
    }]
  }
};

export const degreeOverviewList: DegreeOverviewModel[] = [{
  id: 1,
  name: 'Bachelor of Science in Computer Science',
  degreeTypeName: 'Bachelor',
  startDate: new Date('2015-09-01'),
  endDate: new Date('2018-06-30')
},
{
  id: 2,
  name: 'Master of Artificial Intelligence',
  degreeTypeName: 'Master',
  startDate: new Date('2018-09-01'),
  endDate: new Date('2020-06-30')
},
{
  id: 3,
  name: 'PhD in Machine Learning',
  degreeTypeName: 'Doctorate',
  startDate: new Date('2020-09-01'),
  endDate: null
}];

export const role1: RoleModel = {
  id: 1,
  name: 'Intern',
  isManagement: false
};

export const role2: RoleModel = {
  id: 2,
  name: 'Administrator',
  isManagement: true
};


export const role3: RoleModel = {
  id: 3,
  name: 'Department Head',
  isManagement: true
};

export const rolePointsList1: RolePointsModel[] = [{
  role: role1,
  points: 15
},
{
  role: role2,
  points: 20
}];

export const rolePointsList2: RolePointsModel[] = [{
  role: role2,
  points: 20
}];

export const certificateCalculation1: CertificateCalculationModel = {
  id: 1,
  certificate: certificate1
};

export const certificateCalculation2: CertificateCalculationModel = {
  id: 2,
  certificate: certificate2
};

export const leadershipExperienceCalculation1: LeadershipExperienceCalculationModel = {
  id: 1,
  leadershipExperience: leadershipExperience1
};

export const leadershipExperienceCalculation2: LeadershipExperienceCalculationModel = {
  id: 2,
  leadershipExperience: leadershipExperience2
};

export const degreeCalculation1: DegreeCalculationModel = {
  id: 1,
  degree: degree1,
  weight: 1,
  relevancy: Relevancy.STRONGLY,
  comment: null
};

export const degreeCalculation2: DegreeCalculationModel = {
  id: 2,
  degree: degree2,
  weight: 0.7,
  relevancy: Relevancy.NORMAL,
  comment: 'Still pursuing the degree'
};

export const experienceCalculation1: ExperienceCalculationModel = {
  id: 1,
  experience: experience1,
  relevancy: Relevancy.STRONGLY,
  comment: 'High impact backend development work'
};

export const experienceCalculation2: ExperienceCalculationModel = {
  id: 2,
  experience: experience2,
  relevancy: Relevancy.NORMAL,
  comment: 'Leadership engagement relevant but mixed focus'
};

export const calculation1: CalculationModel = {
  id: 1,
  member: member1,
  role: role1,
  state: CalculationState.ACTIVE,
  publicationDate: new Date('2024-01-20'),
  publicizedBy: 'Admin',
  points: 145,
  certificateCalculations: [certificateCalculation1],
  leadershipExperienceCalculations: [leadershipExperienceCalculation1],
  degreeCalculations: [degreeCalculation1],
  experienceCalculations: [experienceCalculation1]
};

export const calculation2: CalculationModel = {
  id: 2,
  member: member2,
  role: role2,
  state: CalculationState.DRAFT,
  publicationDate: null,
  publicizedBy: null,
  points: 195,
  certificateCalculations: [certificateCalculation2],
  leadershipExperienceCalculations: [leadershipExperienceCalculation2],
  degreeCalculations: [degreeCalculation2],
  experienceCalculations: [experienceCalculation2]
};

export const calculation3: CalculationModel = {
  id: 3,
  member: member3,
  role: role3,
  state: CalculationState.ARCHIVED,
  publicationDate: null,
  publicizedBy: null,
  points: 98,
  certificateCalculations: [],
  leadershipExperienceCalculations: [],
  degreeCalculations: [],
  experienceCalculations: []
};
