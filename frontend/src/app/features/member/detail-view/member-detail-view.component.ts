import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { GLOBAL_DATE_FORMAT } from '../../../shared/format/date-format';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { GenericCvContentComponent } from './generic-cv-content/generic-cv-content.component';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { GenCol } from '../../../shared/generic-table/GenericTableDataSource';
import { DegreeOverviewModel } from './cv/degree-overview.model';
import { ExperienceOverviewModel } from './cv/experience-overview.model';
import { CertificateOverviewModel } from './cv/certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './cv/leadership-experience-overview.model';
import { ColumnTemplateDirective } from '../../../shared/generic-table/column-template/column-template.directive';
import { TranslationScopeDirective } from '../../../shared/translation-scope.directive';

@Component({
  selector: 'app-member-detail-view',
  standalone: true,
  providers: [DatePipe],
  imports: [
    CommonModule,
    DatePipe,
    ScopedTranslationPipe,
    CrudButtonComponent,
    GenericCvContentComponent,
    MatTabGroup,
    MatTab,
    ColumnTemplateDirective,
    TranslationScopeDirective
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrl: './member-detail-view.component.scss'
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  protected readonly GLOBAL_DATE_FORMAT = GLOBAL_DATE_FORMAT;

  readonly member: WritableSignal<MemberModel | null> = signal<MemberModel | null>(null);

  degreeData = signal<DegreeOverviewModel[] | null>(null);

  experienceData = signal<ExperienceOverviewModel[] | null>(null);

  certificateData = signal<CertificateOverviewModel[] | null>(null);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[] | null>(null);

  private readonly datePipe = inject(DatePipe);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.router.navigate(['/member']);
      return;
    }

    this.service.getMemberOverviewByMemberId(Number(id))
      .subscribe({
        next: (memberOverview) => {
          this.member.set(memberOverview.member);
          this.degreeData.set(memberOverview.cv.degrees);
          this.experienceData.set(memberOverview.cv.experiences);
          this.certificateData.set(memberOverview.cv.certificates);
          this.leadershipExperienceData.set(memberOverview.cv.leadershipExperiences);
        }
      });
  }

  degreeColumns: GenCol<DegreeOverviewModel>[] = [GenCol.fromCalculated('dateRange', (e) => {
    const start = this.datePipe.transform(e.startDate, GLOBAL_DATE_FORMAT);
    const end = this.datePipe.transform(e.endDate, GLOBAL_DATE_FORMAT);
    return `${start} - ${end}`;
  }),

  GenCol.fromAttr('name'),
  GenCol.fromAttr('degreeTypeName')];

  experienceColumns: GenCol<ExperienceOverviewModel>[] = [
    GenCol.fromCalculated('dateRange', (e) => {
      const start = this.datePipe.transform(e.startDate, GLOBAL_DATE_FORMAT);
      const end = this.datePipe.transform(e.endDate, GLOBAL_DATE_FORMAT);
      return `${start} - ${end}`;
    }),
    GenCol.fromCalculated('workName', (e: ExperienceOverviewModel) => `${e.employer}\n${e.name}`),
    GenCol.fromAttr('comment'),
    GenCol.fromAttr('experienceTypeName')
  ];

  certificateColumns: GenCol<CertificateOverviewModel>[] = [GenCol.fromAttr('completedAt', [(d: Date) => this.datePipe.transform(d, GLOBAL_DATE_FORMAT)]),
    GenCol.fromAttr('certificateTypeName'),
    GenCol.fromAttr('comment')];

  leadershipExperienceColumns: GenCol<LeadershipExperienceOverviewModel>[] = [GenCol.fromCalculated('leadershipExperienceType', (e) => e.leadershipExperienceType.name),
    GenCol.fromAttr('comment')];
}
