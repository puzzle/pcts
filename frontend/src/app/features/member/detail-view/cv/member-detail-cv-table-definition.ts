import { DegreeOverviewModel } from './degree-overview.model';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { ExperienceOverviewModel } from './experience-overview.model';
import { LeadershipExperienceOverviewModel } from './leadership-experience-overview.model';
import { formatDateLocale } from '../../../../shared/format/date-format';
import { CertificateOverviewModel } from './certificate-overview.model';
import { CalculationModel } from '../../../calculations/calculation.model';
import { CalculationState, calculationStateSortingPriority } from '../../../calculations/calculation-state.enum';
import { inject } from '@angular/core';
import { ScopedTranslationService } from '../../../../shared/i18n-prefix.provider';

const formatRange = (start: Date, end: Date | null): string => {
  const s = formatDateLocale(start);
  const e = end ? formatDateLocale(end) : 'heute';
  return `${s} - ${e}`;
};

export const getDegreeTable = () => new GenericTableDataSource(getDegreeColumns())
  .withLimit(10)
  .withDetailViewLink();

export const getExperienceTable = () => new GenericTableDataSource(getExperienceColumns())
  .withLimit(10)
  .withDetailViewLink();

export const getCertificateTable = () => new GenericTableDataSource(getCertificateColumns())
  .withLimit(10)
  .withDetailViewLink();

export const getLeadershipExperienceTable = () => new GenericTableDataSource(getLeadershipExperienceColumns())
  .withLimit(10)
  .withDetailViewLink();

export const getCalculationTable = () => new GenericTableDataSource(getCalculationColumns())
  .withLimit(10)
  .withDetailViewLink()
  .withSortedBy('state');

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

const getCalculationColumns = (): GenCol<CalculationModel>[] => [
  GenCol.fromCalculated('points', (e: CalculationModel) => {
    return e.points.toFixed(2);
  }),
  GenCol.fromAttr<CalculationModel>('state', [(s: CalculationState) => {
    const scopedTranslationService = inject(ScopedTranslationService);
    return scopedTranslationService.instant(s);
  }])
    .withCustomSortingAccessor((e) => calculationStateSortingPriority[e.state]),
  GenCol.fromAttr('publicizedBy'),
  GenCol.fromAttr('publicationDate', [(d: Date) => {
    return d ? formatDateLocale(d) : '';
  }])
];
