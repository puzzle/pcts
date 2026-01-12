import { Component, OnInit, WritableSignal, signal, inject } from '@angular/core';
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
import { DegreeOverviewModel } from '../degree-overview.model';
import { ExperienceOverviewModel } from '../experience-overview.model';
import { CertificateOverviewModel } from '../certificate-overview.model';
import { LeadershipExperienceOverviewModel } from '../leadership-experience-overview.model';
import { DateTime } from 'luxon';

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
    MatTab
  ],
  templateUrl: './member-detail-view.component.html',
  styleUrl: './member-detail-view.component.scss'
})
export class MemberDetailViewComponent implements OnInit {
  userColumns: { key: keyof User;
    label: string; }[] = [{ key: 'name',
    label: 'Name' },
  { key: 'age',
    label: 'Age' },
  { key: 'email',
    label: 'Email' }];

  userData: User[] = [{ name: 'John Doe',
    age: 30,
    email: 'john@example.com' },
  { name: 'Jane Smith',
    age: 25,
    email: 'jane@example.com' }];


  private readonly service = inject(MemberService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  protected readonly GLOBAL_DATE_FORMAT = GLOBAL_DATE_FORMAT;

  readonly member: WritableSignal<MemberModel | null> = signal<MemberModel | null>(null);

  degreeData = signal<DegreeOverviewModel[] | null>(null);

  experienceData = signal<ExperienceOverviewModel[] | null>(null);

  certificateData = signal<CertificateOverviewModel[] | null>(null);

  leadershipExperienceData = signal<LeadershipExperienceOverviewModel[] | null>(null);


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

  degreeColumns: GenCol<DegreeOverviewModel>[] = [GenCol.fromAttr('startDate'), // use from Calc function
    GenCol.fromAttr('name'),
    GenCol.fromAttr('degreeTypeName')];

  experienceColumns: GenCol<ExperienceOverviewModel>[] = [
    GenCol.fromAttr('startDate'), // use from Calc function
    GenCol.fromAttr('name'), // use form calc with employer
    GenCol.fromAttr('comment'),
    GenCol.fromAttr('experienceTypeName') // custom styling
  ];

  certificateColumns: GenCol<CertificateOverviewModel>[] = [GenCol.fromAttr('completedAt', [(d: DateTime) => d.toLocaleString(DateTime.DATE_MED)]),
    GenCol.fromAttr('certificateTypeName'),
    GenCol.fromAttr('comment')];

  leadershipExperienceColumns: GenCol<LeadershipExperienceOverviewModel>[] = [GenCol.fromCalculated('leadershipExperienceType', (e) => e.leadershipExperienceType.name),
    GenCol.fromAttr('comment')];
}


interface User {
  name: string;
  age: number;
  email: string;
}
