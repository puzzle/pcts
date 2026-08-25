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
import { MemberOverviewModel } from '../member-overview.model';
import { CertificateService } from '../../certificates/certificate.service';
import { CertificateModel } from '../../certificates/certificate.model';
import { AddCertificateComponent } from '../../certificates/add-certificate/add-certificate.component';
import { PctsModalService } from '../../../shared/modal/pcts-modal.service';
import { RolePointsModel } from './RolePointsModel';
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
import { GenericTableDataSourceService } from '../../../shared/generic-table/generic-table-data-source.service';

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
  readonly member: WritableSignal<MemberOverviewModel | null> = signal<MemberOverviewModel | null>(null);

  private readonly modalService = inject(PctsModalService);

  private readonly genericTableDataSourceService = inject(GenericTableDataSourceService);

  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly dialog = inject(PctsModalService);

  private readonly certificateService = inject(CertificateService);

  private readonly degreeService = inject(DegreeService);

  private readonly leadershipExperienceService = inject(LeadershipExperienceService);

  readonly experienceTable = this.genericTableDataSourceService.getExperienceTable();

  readonly certificateTable = this.genericTableDataSourceService.getCertificateTable(this.member());

  readonly degreeTable = this.genericTableDataSourceService.getDegreeTable(this.member());

  readonly leadershipExperienceTable = this.genericTableDataSourceService.getLeadershipExperienceTable(this.member());


  readonly rolePointList = signal<RolePointsModel[]>([]);

  degreeData = signal<DegreeOverviewModel[]>([]);

  experienceData = signal<ExperienceOverviewModel[]>([]);

  certificateData = signal<CertificateOverviewModel[]>([]);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[]>([]);

  tabGroup = viewChild(MatTabGroup);

  tabIndex = input.required<number>();


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

  openDegreeDialog = this.modalService.createDialogOpener<DegreeModel>(AddDegreeComponent, (model) => this.degreeService.addDegree(model), this.member());

  openCertificateDialog = this.modalService.createDialogOpener<CertificateModel>(AddCertificateComponent, (model) => this.certificateService.addCertificate(model), this.member());

  openLeadershipExperienceDialog = this.modalService.createDialogOpener<LeadershipExperienceModel>(AddLeadershipExperienceComponent, (model) => this.leadershipExperienceService.addLeadershipExperience(model), this.member());

  onTabIndexChange(index: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tabIndex: index },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
