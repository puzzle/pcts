import { Component, effect, inject, Signal, signal, viewChild, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { MemberDto } from '../member.dto';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';

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

  dataSource = new MatTableDataSource<MemberDto>();

  sort: Signal<MatSort> = viewChild.required(MatSort);

  members: WritableSignal<MemberDto[]> = signal([]);

  allFilter = true;

  memberFilter = false;

  applicantFilter = false;

  searchText = '';

  constructor() {
    this.service.getAllMembers()
      .subscribe((members: MemberDto[]) => {
        this.members.set(members);
      });

    this.dataSource.filterPredicate = this.createFilterPredicate();

    effect(() => {
      this.dataSource.data = this.members();
      this.dataSource.sort = this.sort();
      this.applyCombinedFilter();
    });

    this.route.queryParams.subscribe((params) => {
      this.searchText = params['q'] ? decodeURIComponent(params['q']) : '';
      const status = params['status'] || 'all';

      this.allFilter = status === 'all';
      this.memberFilter = status.includes('member') && status !== 'all';
      this.applicantFilter = status.includes('applicant') && status !== 'all';

      this.applyCombinedFilter();
    });
  }

  createFilterPredicate(): (data: MemberDto, filter: string) => boolean {
    return (member: MemberDto, filter: string): boolean => {
      const filterValues = JSON.parse(filter);
      const searchTxt = filterValues.text.toLowerCase();
      const status = filterValues.status;

      let statusMatch = false;
      const employmentState = member.employmentState?.toLowerCase() || '';
      if (status === 'all') {
        statusMatch = true;
      } else if (status === 'member' && employmentState === 'member') {
        statusMatch = true;
      } else if (status === 'applicant' && employmentState === 'bewerber') {
        statusMatch = true;
      } else if (status === 'member+applicant' && (employmentState === 'member' || employmentState === 'bewerber')) {
        statusMatch = true;
      }

      const formattedBirthday = this.datePipe.transform(member.birthday, 'dd.MM.yyyy');

      const memberDataString = (
        member.name +
        member.lastName +
        formattedBirthday +
        member.organisationUnit.name +
        member.employmentState
      ).toLowerCase();

      const searchTerms: string[] = searchTxt.toLowerCase()
        .split(' ')
        .filter(Boolean);

      const isTextMatch: boolean = searchTerms.every((term) => memberDataString.includes(term));

      return isTextMatch && statusMatch;
    };
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchText = filterValue;
    this.applyCombinedFilter();
  }

  private applyCombinedFilter() {
    let statusFilterValue = 'none';

    if (this.allFilter) {
      statusFilterValue = 'all';
    } else if (this.memberFilter && this.applicantFilter) {
      statusFilterValue = 'member+applicant';
    } else if (this.memberFilter) {
      statusFilterValue = 'member';
    } else if (this.applicantFilter) {
      statusFilterValue = 'applicant';
    }

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

  toggleFilter(statusFilterValue: string) {
    if (statusFilterValue === 'all') {
      this.allFilter = true;
      this.memberFilter = false;
      this.applicantFilter = false;
    } else {
      const key = statusFilterValue === 'member' ? 'memberFilter' : 'applicantFilter';
      this[key] = !this[key];
      this.allFilter = false;
      if (!this.memberFilter && !this.applicantFilter) {
        this.allFilter = true;
      }
    }
    this.applyCombinedFilter();
  }
}
