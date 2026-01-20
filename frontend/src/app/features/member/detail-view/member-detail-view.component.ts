import { Component, inject, input, OnInit, signal, viewChild, WritableSignal } from '@angular/core';
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
import { AddCertificateComponent } from '../modal-components/add-certificate.component/add-certificate.component';
import { ModalService } from '../../../shared/modal-service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { CertificateModel } from '../../certificates/certificate.model';
import { CertificateService } from '../../certificates/certificate.service';
import { MemberModel } from '../member.model';
import { RolePointsModel } from './RolePointsModel';
import { MemberCalculationTableComponent } from './calculation-table/member-calculation-table.component';

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
    MemberCalculationTableComponent
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrls: ['./member-detail-view.component.scss']
})
export class MemberDetailViewComponent implements OnInit {
  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly dialog = inject(ModalService);

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

  openCertificateDialog(model?: CertificateModel) {
    this.dialog.openModal(AddCertificateComponent, { data: { model } })
      .afterClosed()
      .subscribe((result: { modalSubmitMode: ModalSubmitMode;
        submittedModel: CertificateModel; }) => {
        switch (result.modalSubmitMode) {
          case ModalSubmitMode.SAVE:
            break;
          case ModalSubmitMode.ENTER_ANOTHER:
            this.openCertificateDialog();
            break;
          case ModalSubmitMode.COPY:
            this.openCertificateDialog(result.submittedModel);
            break;
          default:
            result.modalSubmitMode satisfies never;
        }
        result.submittedModel.member = { id: this.member()!.id } as MemberModel;

        this.certificateService.addCertificate(result.submittedModel)
          .subscribe();
      });
  }

  onTabIndexChange(index: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tabIndex: index },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
