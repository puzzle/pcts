import { Component, OnInit, WritableSignal, signal, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { GLOBAL_DATE_FORMAT } from '../../../shared/format/date-format';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { GenericCvContentComponent } from './generic-cv-content/generic-cv-content.component';
import { MatButton } from '@angular/material/button';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { MatTab, MatTabGroup } from '@angular/material/tabs';
import { OrganisationUnitModel } from '../../organisation-unit/organisation-unit.model';
import { GenCol } from '../../../shared/generic-table/GenericTableDataSource';

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
    MatButton,
    GenericTableComponent,
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

  orgData = signal<OrganisationUnitModel[] | null>(null);


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
        }
      });
  }

  orgColumns: GenCol<OrganisationUnitModel>[] = [GenCol.fromAttr('name')];
}


interface User {
  name: string;
  age: number;
  email: string;
}
