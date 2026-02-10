import { Component, DestroyRef, inject, input, OnInit, signal, viewChild, WritableSignal } from '@angular/core';
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
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { CertificateService } from '../../certificates/certificate.service';
import { MemberModel } from '../member.model';
import { CertificateModel } from '../../certificates/certificate.model';
import { AddCertificateComponent } from '../../certificates/add-certificate/add-certificate.component';
import { PctsModalService } from '../../../shared/modal/pcts-modal.service';
import { RolePointsModel } from './RolePointsModel';
import { MemberCalculationTableComponent } from './calculation-table/member-calculation-table.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ShowIfAdminDirective } from '../../../core/auth/directive/show-if-admin.directive';

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
    TranslationScopeDirective,
    MemberCalculationTableComponent,
    ShowIfAdminDirective
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrls: ['./member-detail-view.component.scss']
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly dialog = inject(PctsModalService);

  private readonly certificateService = inject(CertificateService);

  readonly experienceTable = getExperienceTable();

  readonly certificateTable = getCertificateTable();

  readonly degreeTable = getDegreeTable();

  readonly leadershipExperienceTable = getLeadershipExperienceTable();

  readonly member: WritableSignal<MemberOverviewModel | null> = signal<MemberOverviewModel | null>(null);

  readonly rolePointList = signal<RolePointsModel[]>([]);

  degreeData = signal<DegreeOverviewModel[]>([]);

  experienceData = signal<ExperienceOverviewModel[]>([]);

  certificateData = signal<CertificateOverviewModel[]>([]);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[]>([]);

  tabGroup = viewChild(MatTabGroup);

  tabIndex = input.required<number>();

  private readonly destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    this.getData();
  }

  getData() {
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
    this.service.getPointsForActiveCalculationsForRoleByMemberId(Number(id))
      .subscribe({
        next: (RolePoints) => {
          this.rolePointList.set(RolePoints);
          const tabGroup = this.tabGroup();
          if (tabGroup) {
            tabGroup.selectedIndex = this.tabIndex();
          }
        }
      });
  }

  openCertificateDialog = (model?: CertificateModel) => {
    this.dialog.openModal(AddCertificateComponent, { data: model })
      .afterSubmitted
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(({ modalSubmitMode, submittedModel }) => {
        const memberId = this.member()?.id;
        if (memberId) {
          submittedModel.member = { id: memberId } as MemberModel;
        } else {
          // Prevent running if member id is null
          return;
        }
        switch (modalSubmitMode) {
          case ModalSubmitMode.SAVE:
            break;
          case ModalSubmitMode.ENTER_ANOTHER:
            this.openCertificateDialog();
            break;
          case ModalSubmitMode.COPY:
            this.openCertificateDialog(submittedModel);
            break;
          default:
            modalSubmitMode satisfies never;
        }
        this.certificateService
          .addCertificate(submittedModel)
          .subscribe(() => this.getData());
      });
  };

  onTabIndexChange(index: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tabIndex: index },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
