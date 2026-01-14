import { formatDate } from '@angular/common';
import { GLOBAL_DATE_FORMAT } from '../../../../shared/format/date-format';
import { DegreeOverviewModel } from './degree-overview.model';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { ExperienceOverviewModel } from './experience-overview.model';
import { CertificateOverviewModel } from './certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './leadership-experience-overview.model';

const formatRange = (start: Date, end: Date | null, locale: string): string => {
  const s = formatDate(start, GLOBAL_DATE_FORMAT, locale);
  const e = end ? formatDate(end, GLOBAL_DATE_FORMAT, locale) : 'heute';
  return `${s} - ${e}`;
};

export const getDegreeColumns = (locale: string): GenCol<DegreeOverviewModel>[] => [GenCol.fromCalculated('dateRange', (e: DegreeOverviewModel) => formatRange(e.startDate, e.endDate, locale))
  .withCustomSortingAccessor((e: DegreeOverviewModel) => new Date(e.startDate)
    .getTime()),
GenCol.fromAttr('name'),
GenCol.fromAttr('degreeTypeName')];

export const getExperienceTable = (locale: string): GenericTableDataSource<ExperienceOverviewModel> => new GenericTableDataSource(getExperienceColumns(locale))
  .withLimit(4);

export const getExperienceColumns = (locale: string): GenCol<ExperienceOverviewModel>[] => [
  GenCol.fromCalculated('dateRange', (e: ExperienceOverviewModel) => formatRange(e.startDate, e.endDate, locale))
    .withCustomSortingAccessor((e: ExperienceOverviewModel) => new Date(e.startDate)
      .getTime()),
  GenCol.fromCalculated('workName', (e: ExperienceOverviewModel) => `${e.employer}\n${e.name}`),
  GenCol.fromAttr('comment'),
  GenCol.fromAttr('experienceTypeName')
];

export const getCertificateColumns = (locale: string): GenCol<CertificateOverviewModel>[] => [GenCol.fromAttr('completedAt', [(d: Date) => {
  return d ? formatDate(d, GLOBAL_DATE_FORMAT, locale) : '';
}]),
GenCol.fromAttr('certificateTypeName'),
GenCol.fromAttr('comment')];

export const getLeadershipColumns = (): GenCol<LeadershipExperienceOverviewModel>[] => [GenCol.fromCalculated('leadershipExperienceType', (e: LeadershipExperienceOverviewModel) => {
  return e.leadershipExperienceType ? e.leadershipExperienceType.name : '';
}),
GenCol.fromAttr('comment')];
