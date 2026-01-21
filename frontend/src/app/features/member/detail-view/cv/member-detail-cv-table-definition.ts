import { DegreeOverviewModel } from './degree-overview.model';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { ExperienceOverviewModel } from './experience-overview.model';
import { LeadershipExperienceOverviewModel } from './leadership-experience-overview.model';
import { formatDateLocale } from '../../../../shared/format/date-format';
import { CertificateOverviewModel } from './certificate-overview.model';

const formatRange = (start: Date, end: Date | null): string => {
  const s = formatDateLocale(start);
  const e = end ? formatDateLocale(end) : 'heute';
  return `${s} - ${e}`;
};

export const getDegreeTable = () => new GenericTableDataSource(getDegreeColumns())
  .withLimit(10)
  .withLink();

export const getExperienceTable = () => new GenericTableDataSource(getExperienceColumns())
  .withLimit(10)
  .withLink();

export const getCertificateTable = () => new GenericTableDataSource(getCertificateColumns())
  .withLimit(10)
  .withLink();

export const getLeadershipExperienceTable = () => new GenericTableDataSource(getLeadershipExperienceColumns())
  .withLimit(10)
  .withLink();

const getDegreeColumns = (): GenCol<DegreeOverviewModel>[] => [GenCol.fromCalculated('dateRange', (e: DegreeOverviewModel) => formatRange(e.startDate, e.endDate))
  .withCustomSortingAccessor((e: DegreeOverviewModel) => new Date(e.startDate)
    .getTime()),
GenCol.fromAttr<DegreeOverviewModel>('name'),
GenCol.fromAttr('degreeTypeName')];

const getExperienceColumns = (): GenCol<ExperienceOverviewModel>[] => [
  GenCol.fromCalculated('dateRange', (e: ExperienceOverviewModel) => formatRange(e.startDate, e.endDate))
    .withCustomSortingAccessor((e: ExperienceOverviewModel) => new Date(e.startDate)
      .getTime()),
  GenCol.fromCalculated('workName', (e: ExperienceOverviewModel) => `${e.employer}\n${e.name}`),
  GenCol.fromAttr('comment'),
  GenCol.fromAttr('experienceTypeName')
];

const getCertificateColumns = (): GenCol<CertificateOverviewModel>[] => [GenCol.fromAttr('completedAt', [(d: Date) => {
  return d ? formatDateLocale(d) : '';
}]),
GenCol.fromAttr('certificateTypeName'),
GenCol.fromAttr('comment')];

const getLeadershipExperienceColumns = (): GenCol<LeadershipExperienceOverviewModel>[] => [GenCol.fromCalculated('leadershipExperienceType', (e: LeadershipExperienceOverviewModel) => {
  return e.leadershipExperienceType ? e.leadershipExperienceType.name : '';
}),
GenCol.fromAttr('comment')];
