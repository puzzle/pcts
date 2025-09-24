import { Component, effect, inject, Signal, signal, viewChild, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-member-overview.component',
  providers: [DatePipe],
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    DatePipe,
    MatIcon,
    MatButton
  ],
  templateUrl: './member-overview.component.html',
  styleUrl: './member-overview.component.css'
})
export class MemberOverviewComponent {
  private service: MemberService = inject(MemberService);

  private datePipe: DatePipe = inject(DatePipe);

  private router = inject(Router);

  private route = inject(ActivatedRoute);

  displayedColumns: string[] = [
    'name',
    'birthday',
    'organisation_unit',
    'status'
  ];

  dataSource: MatTableDataSource<MemberModel> = new MatTableDataSource<MemberModel>();

  sort: Signal<MatSort> = viewChild.required(MatSort);

  members: WritableSignal<MemberModel[]> = signal([]);

  allFilter = true;

  memberFilter = false;

  applicantFilter = false;

  exMemberFilter = false;

  searchText = '';

  constructor() {
    this.service.getAllMembers()
      .subscribe((members: MemberModel[]): void => {
        this.members.set(members);
      });

    this.dataSource.filterPredicate = this.createFilterPredicate();

    effect((): void => {
      this.dataSource.data = this.members();
      this.dataSource.sort = this.sort();
      this.applyCombinedFilter();
    });

    this.route.queryParams.subscribe((params: Params): void => {
      this.searchText = params['q'] ? decodeURIComponent(params['q']) : '';

      const status: string = params['status'] || 'all';
      const statuses: string[] = status.split('+');

      this.allFilter = status === 'all';
      this.memberFilter = statuses.includes('member');
      this.applicantFilter = statuses.includes('applicant');
      this.exMemberFilter = statuses.includes('ex-member');

      this.applyCombinedFilter();
    });
  }

  createFilterPredicate(): (data: MemberModel, filter: string) => boolean {
    return (member: MemberModel, filter: string): boolean => {
      const filterValues = JSON.parse(filter);
      const searchTxt: string = filterValues.text.toLowerCase();
      const status: string = filterValues.status;

      const statuses: string[] = status.split('+');
      const employmentState: string = member.employmentState?.toLowerCase() || '';

      const statusMatch: boolean = status === 'all' || statuses.includes(employmentState);


      const formattedBirthday: string | null = this.datePipe.transform(member.birthday, 'dd.MM.yyyy');

      const memberDataString: string = (
        member.name +
        member.lastName +
        formattedBirthday +
        member.organisationUnit.name +
        member.employmentState
      ).toLowerCase();

      const searchTerms: string[] = searchTxt.split(' ')
        .filter(Boolean);

      const isTextMatch: boolean = searchTerms.every((term) => memberDataString.includes(term));

      return isTextMatch && statusMatch;
    };
  }

  applyFilter(searchText: string): void {
    this.searchText = searchText;
    this.applyCombinedFilter();
  }

  private applyCombinedFilter(): void {
    const selected: string[] = [];
    if (this.memberFilter) {
      selected.push('member');
    }
    if (this.applicantFilter) {
      selected.push('applicant');
    }
    if (this.exMemberFilter) {
      selected.push('ex-member');
    }

    const statusFilterValue: string = selected.length ? selected.join('+') : 'all';


    const combinedFilter = {
      text: this.searchText,
      status: statusFilterValue
    };
    this.dataSource.filter = JSON.stringify(combinedFilter);

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: this.searchText ? encodeURIComponent(this.searchText) : null,
        status: statusFilterValue !== 'all' ? statusFilterValue : null
      },
      queryParamsHandling: 'merge'
    });
  }


  toggleFilter(statusFilterValue: string): void {
    switch (statusFilterValue) {
      case 'applicant':
        this.applicantFilter = !this.applicantFilter;
        this.allFilter = false;
        break;
      case 'ex-member':
        this.exMemberFilter = !this.exMemberFilter;
        this.allFilter = false;
        break;
      case 'member':
        this.memberFilter = !this.memberFilter;
        this.allFilter = false;
        break;
      default:
        this.allFilter = true;
        this.memberFilter = false;
        this.applicantFilter = false;
        this.exMemberFilter = false;
    }
    this.applyCombinedFilter();
  }
}
