import { inject, Injectable } from '@angular/core';
import { GenCol, GenericTableDataSource } from './generic-table-data-source';
import { DegreeOverviewModel } from '../../features/member/detail-view/cv/degree-overview.model';
import { ExperienceOverviewModel } from '../../features/member/detail-view/cv/experience-overview.model';
import { CertificateOverviewModel } from '../../features/member/detail-view/cv/certificate-overview.model';
import { formatDateLocale } from '../format/date-format';
import {
  LeadershipExperienceOverviewModel
} from '../../features/member/detail-view/cv/leadership-experience-overview.model';
import { CalculationModel } from '../../features/calculations/calculation.model';
import { CalculationState, calculationStateSortingPriority } from '../../features/calculations/calculation-state.enum';
import { ScopedTranslationService } from '../i18n-prefix.provider';
import { PctsModalService } from '../modal/pcts-modal.service';
import { DegreeService } from '../../features/degrees/degree.service';
import { AddDegreeComponent } from '../../features/degrees/add-degree/add-degree.component';
import { DegreeModel } from '../../features/degrees/degree.model';
import { MemberOverviewModel } from '../../features/member/member-overview.model';
import { CertificateModel } from '../../features/certificates/certificate.model';
import { AddCertificateComponent } from '../../features/certificates/add-certificate/add-certificate.component';
import { CertificateService } from '../../features/certificates/certificate.service';
import { LeadershipExperienceModel } from '../../features/leadership-experiences/leadership-experience.model';
import {
  AddLeadershipExperienceComponent
} from '../../features/leadership-experiences/add-leadership-experience/add-leadership-experience.component';
import { LeadershipExperienceService } from '../../features/leadership-experiences/leadership-experience.service';

@Injectable({ providedIn: 'root' })
export class GenericTableDataSourceService {
  private readonly scopedTranslationService = inject(ScopedTranslationService);

  private readonly modalService = inject(PctsModalService);

  private readonly degreeService = inject(DegreeService);

  private readonly certificateService = inject(CertificateService);

  private readonly leadershipExperienceService = inject(LeadershipExperienceService);

  public getDegreeTable(member: MemberOverviewModel | null) {
    const editDegree = this.modalService.createDialogOpener<DegreeModel>(AddDegreeComponent, (model) => this.degreeService.updateDegree(model?.id, model), member);

    return new GenericTableDataSource(this.getDegreeColumns(), editDegree)
      .withLimit(10)
      .withDetailViewLink();
  }

  public getExperienceTable() {
    return new GenericTableDataSource(this.getExperienceColumns())
      .withLimit(10)
      .withDetailViewLink();
  }

  public getCertificateTable(member: MemberOverviewModel | null) {
    const editCertificate = this.modalService.createDialogOpener<CertificateModel>(AddCertificateComponent, (model) => this.certificateService.updateCertificate(model?.id, model), member);

    return new GenericTableDataSource(this.getCertificateColumns(), editCertificate)
      .withLimit(10)
      .withDetailViewLink();
  }

  public getLeadershipExperienceTable(member: MemberOverviewModel | null) {
    const editLeadershipExperience = this.modalService.createDialogOpener<LeadershipExperienceModel>(AddLeadershipExperienceComponent, (model) => this.leadershipExperienceService.updateLeadershipExperience(model?.id, model), member);

    return new GenericTableDataSource(this.getLeadershipExperienceColumns(), editLeadershipExperience)
      .withLimit(10)
      .withDetailViewLink();
  }

  public getCalculationTable() {
    return new GenericTableDataSource(this.getCalculationColumns())
      .withLimit(10)
      .withDetailViewLink()
      .withSortedBy('state');
  }

  private getDegreeColumns(): GenCol<DegreeOverviewModel>[] {
    return [GenCol.fromCalculated('dateRange', (e: DegreeOverviewModel) => this.formatRange(e.startDate, e.endDate))
      .withCustomSortingAccessor((e: DegreeOverviewModel) => new Date(e.startDate)
        .getTime()),
    GenCol.fromAttr<DegreeOverviewModel>('name'),
    GenCol.fromAttr('degreeTypeName')];
  }

  private getExperienceColumns(): GenCol<ExperienceOverviewModel>[] {
    return [
      GenCol.fromCalculated('dateRange', (e: ExperienceOverviewModel) => this.formatRange(e.startDate, e.endDate))
        .withCustomSortingAccessor((e: ExperienceOverviewModel) => new Date(e.startDate)
          .getTime()),
      GenCol.fromCalculated('workName', (e: ExperienceOverviewModel) => `${e.employer}\n${e.name}`),
      GenCol.fromAttr('comment'),
      GenCol.fromAttr('experienceTypeName')
    ];
  }

  private getCertificateColumns(): GenCol<CertificateOverviewModel>[] {
    return [GenCol.fromAttr('completedAt', [(d: Date) => {
      return d ? formatDateLocale(d) : '';
    }]),
    GenCol.fromAttr('certificateTypeName'),
    GenCol.fromAttr('comment')];
  }

  private getLeadershipExperienceColumns(): GenCol<LeadershipExperienceOverviewModel>[] {
    return [GenCol.fromCalculated('leadershipExperienceType', (e: LeadershipExperienceOverviewModel) => {
      return e.leadershipExperienceType ? e.leadershipExperienceType.name : '';
    }),
    GenCol.fromAttr('comment')];
  }

  private getCalculationColumns(): GenCol<CalculationModel>[] {
    return [
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
  }

  private formatRange(start: Date, end: Date | null): string {
    const s = formatDateLocale(start);
    const e = end ? formatDateLocale(end) : 'heute';
    return `${s} - ${e}`;
  }
}
