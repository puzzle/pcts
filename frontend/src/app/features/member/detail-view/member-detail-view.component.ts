import { Component, computed, inject, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
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
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import {
  getCertificateTable,
  getDegreeTable,
  getExperienceTable,
  getLeadershipExperienceTable
} from './cv/member-detail-cv-table-definition';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';

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

  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly certificateService = inject(CertificateService);

  private readonly degreeService = inject(DegreeService);

  private readonly leadershipExperienceService = inject(LeadershipExperienceService);

  private readonly memberId = toSignal(this.route.paramMap.pipe(map((params) => Number(params.get('id')))), { initialValue: 0 });

  readonly memberResource = rxResource({
    params: () => this.memberId(),
    stream: ({ params: id }) => this.service.getMemberOverviewByMemberId(Number(id))
  });

  readonly rolePointsResource = rxResource({
    params: () => this.memberId(),
    stream: ({ params: id }) => this.service.getPointsForActiveCalculationsForRoleByMemberId(Number(id))
  });

  readonly member = computed(() => this.memberResource.value()?.member ?? null);

  readonly rolePointList = computed(() => this.rolePointsResource.value() ?? []);

  degreeData = computed(() => this.memberResource.value()?.cv.degrees ?? []);

  experienceData = computed(() => this.memberResource.value()?.cv.experiences ?? []);

  certificateData = computed(() => this.memberResource.value()?.cv.certificates ?? []);

  leadershipExperienceData = computed(() => this.memberResource.value()?.cv.leadershipExperiences ?? []);

  tabIndex = input.required<number>();

  readonly experienceTable = getExperienceTable();

  readonly certificateTable = getCertificateTable();

  readonly degreeTable = getDegreeTable();

  readonly leadershipExperienceTable = getLeadershipExperienceTable();

  openDegreeDialog = this.modalService.createDialogOpener<DegreeModel>(
    AddDegreeComponent, this.member, (model: DegreeModel) => this.degreeService.addDegree(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  openCertificateDialog = this.modalService.createDialogOpener<CertificateModel>(
    AddCertificateComponent, this.member, (model: CertificateModel) => this.certificateService.addCertificate(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  openLeadershipExperienceDialog = this.modalService.createDialogOpener<LeadershipExperienceModel>(
    AddLeadershipExperienceComponent, this.member, (model: LeadershipExperienceModel) => this.leadershipExperienceService.addLeadershipExperience(model), () => this.memberResource.reload(), [ModalSubmitMode.ENTER_ANOTHER,
      ModalSubmitMode.COPY]
  );

  onTabIndexChange(index: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tabIndex: index },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
