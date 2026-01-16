import { DegreeOverviewModel } from './degree-overview.model';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { ExperienceOverviewModel } from './experience-overview.model';
import { CertificateOverviewModel } from './certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './leadership-experience-overview.model';
import { formatDateLocale } from '../../../../shared/format/date-format';

const formatRange = (start: Date, end: Date | null): string => {
  const s = formatDateLocale(start);
  const e = end ? formatDateLocale(end) : 'heute';
  return `${s} - ${e}`;
};

export const getDegreeTable = (): GenericTableDataSource<DegreeOverviewModel> => new GenericTableDataSource(getDegreeColumns());

export const getDegreeColumns = (): GenCol<DegreeOverviewModel>[] => [GenCol.fromCalculated('dateRange', (e: DegreeOverviewModel) => formatRange(e.startDate, e.endDate))
  .withCustomSortingAccessor((e: DegreeOverviewModel) => new Date(e.startDate)
    .getTime()),
GenCol.fromAttr<DegreeOverviewModel>('name'),
GenCol.fromAttr('degreeTypeName')];

export const getExperienceTable = (): GenericTableDataSource<ExperienceOverviewModel> => new GenericTableDataSource(getExperienceColumns())
  .withLimit(5);

export const getExperienceColumns = (): GenCol<ExperienceOverviewModel>[] => [
  GenCol.fromCalculated('dateRange', (e: ExperienceOverviewModel) => formatRange(e.startDate, e.endDate))
    .withCustomSortingAccessor((e: ExperienceOverviewModel) => new Date(e.startDate)
      .getTime()),
  GenCol.fromCalculated('workName', (e: ExperienceOverviewModel) => `${e.employer}\n${e.name}`),
  GenCol.fromAttr('comment'),
  GenCol.fromAttr('experienceTypeName')
];

export const getCertificateTable = (): GenericTableDataSource<CertificateOverviewModel> => new GenericTableDataSource(getCertificateColumns());

export const getCertificateColumns = (): GenCol<CertificateOverviewModel>[] => [GenCol.fromAttr('completedAt', [(d: Date) => {
  return d ? formatDateLocale(d) : '';
}]),
GenCol.fromAttr('certificateTypeName'),
GenCol.fromAttr('comment')];

export const getLeadershipExperienceTable = (): GenericTableDataSource<LeadershipExperienceOverviewModel> => new GenericTableDataSource(getLeadershipExperienceColumns());

export const getLeadershipExperienceColumns = (): GenCol<LeadershipExperienceOverviewModel>[] => [GenCol.fromCalculated('leadershipExperienceType', (e: LeadershipExperienceOverviewModel) => {
  return e.leadershipExperienceType ? e.leadershipExperienceType.name : '';
}),
GenCol.fromAttr('comment')];
