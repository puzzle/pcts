import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
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
  getCertificateTable,
  getDegreeTable,
  getExperienceTable,
  getLeadershipExperienceTable
} from './cv/member-detail-cv-table-definition';
import { MemberOverviewModel } from '../member-overview.model';

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
  templateUrl: './member-detail-view.component.html'
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  readonly member: WritableSignal<MemberOverviewModel | null> = signal<MemberOverviewModel | null>(null);

  degreeData = signal<DegreeOverviewModel[]>([]);

  experienceData = signal<ExperienceOverviewModel[]>([]);

  certificateData = signal<CertificateOverviewModel[]>([]);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[]>([]);

  readonly experienceTable = getExperienceTable();

  readonly certificateTable = getCertificateTable();

  readonly degreeTable = getDegreeTable();

  readonly leadershipExperienceTable = getLeadershipExperienceTable();

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
