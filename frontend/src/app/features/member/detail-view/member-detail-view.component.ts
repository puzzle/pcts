import { Component, computed, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { GenericCvContentComponent } from './generic-cv-content/generic-cv-content.component';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { TranslationScopeDirective } from '../../../shared/translation-scope/translation-scope.directive';
import { CertificateService } from '../../certificates/certificate.service';
import { CertificateModel } from '../../certificates/certificate.model';
import { AddCertificateComponent } from '../../certificates/add-certificate/add-certificate.component';
import { PctsModalService } from '../../../shared/modal/pcts-modal.service';
import { MemberCalculationTableComponent } from './calculation-table/member-calculation-table.component';
import { LeadershipExperienceModel } from '../../leadership-experiences/leadership-experience.model';
import {
  AddLeadershipExperienceComponent
} from '../../leadership-experiences/add-leadership-experience/add-leadership-experience.component';
import { LeadershipExperienceService } from '../../leadership-experiences/leadership-experience.service';
import { ShowIfAdminDirective } from '../../../core/auth/directive/show-if-admin.directive';
import { DegreeModel } from '../../degrees/degree.model';
import { AddDegreeComponent } from '../../degrees/add-degree/add-degree.component';
import { DegreeService } from '../../degrees/degree.service';
import {
  getCertificateTable,
  getDegreeTable,
  getExperienceTable,
  getLeadershipExperienceTable
} from './cv/member-detail-cv-table-definition';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { rxResource } from '@angular/core/rxjs-interop';
import { MemberService } from '../member.service';
import { DegreeOverviewModel } from './cv/degree-overview.model';
import { CertificateOverviewModel } from './cv/certificate-overview.model';
import { LeadershipExperienceOverviewModel } from './cv/leadership-experience-overview.model';

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
export class MemberDetailViewComponent {
  private readonly modalService = inject(PctsModalService);

  private readonly memberService = inject(MemberService);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly certificateService = inject(CertificateService);

  private readonly degreeService = inject(DegreeService);

  private readonly leadershipExperienceService = inject(LeadershipExperienceService);

  tabIndex = input.required<number>();

  memberId = input.required<number>();

  readonly memberResource = rxResource({
    params: () => this.memberId(),
    stream: ({ params: id }) => this.memberService.getMemberOverviewByMemberId(id)
  });

  readonly rolePointsResource = rxResource({
    params: () => this.memberId(),
    stream: ({ params: id }) => this.memberService.getPointsForActiveCalculationsForRoleByMemberId(id)
  });

  rolePointList = computed(() => this.rolePointsResource.value() ?? []);

  degreeData = computed(() => this.memberResource.value()?.cv.degrees ?? []);

  experienceData = computed(() => this.memberResource.value()?.cv.experiences ?? []);

  certificateData = computed(() => this.memberResource.value()?.cv.certificates ?? []);

  leadershipExperienceData = computed(() => this.memberResource.value()?.cv.leadershipExperiences ?? []);

  readonly experienceTable = getExperienceTable();

  readonly certificateTable = getCertificateTable();

  readonly degreeTable = getDegreeTable();

  readonly leadershipExperienceTable = getLeadershipExperienceTable();

  addDegreeDialog = this.modalService.createDialogOpener<DegreeModel>(
    AddDegreeComponent, (model: DegreeModel) => this.degreeService.addDegree(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  addCertificateDialog = this.modalService.createDialogOpener<CertificateModel>(
    AddCertificateComponent, (model: CertificateModel) => this.certificateService.addCertificate(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  addLeadershipExperienceDialog = this.modalService.createDialogOpener<LeadershipExperienceModel>(
    AddLeadershipExperienceComponent, (model: LeadershipExperienceModel) => this.leadershipExperienceService.addLeadershipExperience(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  private createEditDegreeDialog = this.modalService.createDialogOpener<DegreeModel>(
    AddDegreeComponent, (model: DegreeModel) => this.degreeService.updateDegree(model.id, model), () => this.memberResource.reload(), []
  );

  editDegreeDialog(row: DegreeOverviewModel) {
    this.degreeService.getDegreeById(row.id)
      .subscribe((degree: DegreeModel) => {
        this.createEditDegreeDialog(degree);
      });
  }

  private createEditCertificateDialog = this.modalService.createDialogOpener<CertificateModel>(
    AddCertificateComponent, (model: CertificateModel) => this.certificateService.updateCertificate(model.id, model), () => this.memberResource.reload(), []
  );

  editCertificateDialog(row: CertificateOverviewModel) {
    this.certificateService.getCertificateById(row.id)
      .subscribe((certificate: CertificateModel) => {
        this.createEditCertificateDialog(certificate);
      });
  }

  private createEditLeadershipExperienceDialog = this.modalService.createDialogOpener<LeadershipExperienceModel>(
    AddLeadershipExperienceComponent, (model: LeadershipExperienceModel) => this.leadershipExperienceService.updateLeadershipExperience(model.id, model), () => this.memberResource.reload(), []
  );

  editLeadershipExperienceDialog(row: LeadershipExperienceOverviewModel) {
    this.leadershipExperienceService.getLeadershipExperienceById(row.id)
      .subscribe((leadershipExperience: LeadershipExperienceModel) => {
        this.createEditLeadershipExperienceDialog(leadershipExperience);
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
