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
import { ActivatedRoute, Params, Router, RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';

@Component({
  selector: 'app-member-overview',
  standalone: true,
  providers: [DatePipe],
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    DatePipe,
    MatIcon,
    MatButton,
    TranslatePipe,
    RouterLink
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

  activeFilters = new Set<string>();

  searchText = '';

  employmentStateValues = Object.values(EmploymentState);

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

      this.activeFilters.clear();
      const status: string = params['status'];
      if (status) {
        status.split('+')
          .forEach((s) => this.activeFilters.add(s));
      }

      this.applyCombinedFilter();
    });
  }

  createFilterPredicate(): (data: MemberModel, filter: string) => boolean {
    return (member: MemberModel, filter: string): boolean => {
      const filterValues = JSON.parse(filter);
      const searchTxt: string = filterValues.text.toLowerCase();
      const status: string = filterValues.status;

      const statuses: string[] = status.split('+');
      const employmentState: string = member.employmentState || '';

      const statusMatch: boolean = status === '' || statuses.includes(employmentState);

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

  applyCombinedFilter(): void {
    const selected: string[] = Array.from(this.activeFilters);
    const statusFilterValue: string = selected.length ? selected.join('+') : '';

    const combinedFilter = {
      text: this.searchText,
      status: statusFilterValue
    };

    this.dataSource.filter = JSON.stringify(combinedFilter);

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: this.searchText ? encodeURIComponent(this.searchText) : null,
        status: statusFilterValue !== '' ? statusFilterValue : null
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  toggleFilter(statusFilterValue: string): void {
    if (this.activeFilters.has(statusFilterValue)) {
      this.activeFilters.delete(statusFilterValue);
    } else {
      this.activeFilters.add(statusFilterValue);
    }
    this.applyCombinedFilter();
  }

  isFilterActive(status: string): boolean {
    return this.activeFilters.has(status);
  }

  isAllFilterActive(): boolean {
    const all = this.activeFilters.size === 0 || this.activeFilters.size === this.employmentStateValues.length;
    if (all) {
      this.employmentStateValues.forEach((s) => this.activeFilters.delete(s));
    }
    return all;
  }
}
