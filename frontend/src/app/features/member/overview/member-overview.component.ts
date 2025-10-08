import { Component, effect, inject, Signal, signal, viewChild, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { debounceTime } from 'rxjs/operators';


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
  styleUrl: './member-overview.component.scss'
})
export class MemberOverviewComponent {
  private readonly service: MemberService = inject(MemberService);

  private readonly datePipe: DatePipe = inject(DatePipe);

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly translate = inject(TranslateService);

  displayedColumns: string[] = [
    'name',
    'birthDate',
    'organisation_unit',
    'status'
  ];

  dataSource: MatTableDataSource<MemberModel> = new MatTableDataSource<MemberModel>();

  sort: Signal<MatSort> = viewChild.required(MatSort);

  members: WritableSignal<MemberModel[]> = signal([]);

  activeFilters = new Set<EmploymentState>();

  searchControl = new FormControl('');

  employmentStateValues: EmploymentState[] = Object.values(EmploymentState);

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

    this.route.data.subscribe(({ filters }) => {
      this.searchControl.setValue(filters.searchText, { emitEvent: false });
      this.activeFilters = new Set(filters.statuses);
      this.applyCombinedFilter();
    });
    console.log();

    this.searchControl.valueChanges
      .pipe(debounceTime(300))
      .subscribe(() => {
        this.applyCombinedFilter();
      });
  }

  createFilterPredicate(): (data: MemberModel, filter: string) => boolean {
    return (member: MemberModel, filter: string): boolean => {
      const filterValues = JSON.parse(filter);
      const searchTxt: string = filterValues.text.toLowerCase();
      const status: string = filterValues.status;

      const statusMatch: boolean = status === '' || status.split('+')
        .includes(member.employmentState);

      const memberDataString: string = (
        member.name +
        member.lastName +
        this.datePipe.transform(member.birthDate, 'dd.MM.yyyy') +
        member.organisationUnit.name +
        this.translate.instant('MEMBER.EMPLOYMENT_STATUS_VALUES.' + member.employmentState)
      ).toLowerCase();

      const searchTerms: string[] = searchTxt.split(' ')
        .filter(Boolean);

      const isTextMatch: boolean = searchTerms.every((term) => memberDataString.includes(term));

      return isTextMatch && statusMatch;
    };
  }

  applyCombinedFilter(): void {
    const selected: string[] = Array.from(this.activeFilters);
    const statusFilterValue: string = selected.length ? selected.join('+') : '';

    this.dataSource.filter = JSON.stringify({
      text: this.searchControl.value ?? '',
      status: statusFilterValue
    });

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: this.searchControl.value ? encodeURIComponent(this.searchControl.value) : null,
        status: statusFilterValue !== '' ? statusFilterValue : null
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  toggleFilter(statusFilterValue: EmploymentState): void {
    if (this.activeFilters.has(statusFilterValue)) {
      this.activeFilters.delete(statusFilterValue);
    } else {
      this.activeFilters.add(statusFilterValue);
    }
    this.applyCombinedFilter();
  }

  isFilterActive(status: EmploymentState): boolean {
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
