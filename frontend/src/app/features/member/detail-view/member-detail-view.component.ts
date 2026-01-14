import { Component, OnInit, signal, WritableSignal, inject, LOCALE_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { GLOBAL_DATE_FORMAT } from '../../../shared/format/date-format';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { GenericCvContentComponent } from './generic-cv-content/generic-cv-content.component';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { DegreeOverviewModel } from './cv/degree-overview.model';
import { ExperienceOverviewModel } from './cv/experience-overview.model';
import { CertificateOverviewModel } from './cv/certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './cv/leadership-experience-overview.model';
import { TranslationScopeDirective } from '../../../shared/translation-scope/translation-scope.directive';
import {
  getCertificateColumns,
  getDegreeColumns,
  getExperienceColumns, getExperienceTable,
  getLeadershipColumns
} from './cv/member-detail-cv-table-definition';

@Component({
  selector: 'app-member-detail-view',
  standalone: true,
  imports: [
    CommonModule,
    ScopedTranslationPipe,
    CrudButtonComponent,
    GenericCvContentComponent,
    MatTabGroup,
    MatTab,
    TranslationScopeDirective
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrl: './member-detail-view.component.scss'
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly locale = inject(LOCALE_ID);

  protected readonly GLOBAL_DATE_FORMAT = GLOBAL_DATE_FORMAT;

  readonly member: WritableSignal<MemberModel | null> = signal<MemberModel | null>(null);

  degreeData = signal<DegreeOverviewModel[] | null>(null);

  experienceData = signal<ExperienceOverviewModel[] | null>(null);

  certificateData = signal<CertificateOverviewModel[] | null>(null);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[] | null>(null);

  readonly degreeColumns = getDegreeColumns(this.locale);

  readonly experienceColumns = getExperienceColumns(this.locale);

  readonly experienceTable = getExperienceTable(this.locale);

  readonly certificateColumns = getCertificateColumns(this.locale);

  readonly leadershipExperienceColumns = getLeadershipColumns();

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
}
