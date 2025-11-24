import { Component, computed, effect, inject, input, OnInit, Signal, signal, WritableSignal } from '@angular/core';
import { MemberService } from '../member.service';
import { MemberModel } from '../member.model';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSortModule } from '@angular/material/sort';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/GenericTableDataSource';
import { DateTime } from 'luxon';
import { ScopedTranslationService } from '../../../shared/services/scoped-translation.service';
import { map, startWith } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import { MemberOverviewParams } from './member-overview-resolver';


type TypedAbstractControl = AbstractControl & { getRawValue: () => object };

export type RequiredFormRawValue<T extends TypedAbstractControl> =
  Required<ReturnType<T['getRawValue']>>;

@Component({
  selector: 'app-member-overview',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatIcon,
    MatButton,
    ScopedTranslationPipe,
    GenericTableComponent,
    CrudButtonComponent
  ],
  templateUrl: './member-overview.component.html',
  styleUrl: './member-overview.component.scss'
})
export class MemberOverviewComponent implements OnInit {
  readonly filters = input.required<MemberOverviewParams>();

  members: WritableSignal<MemberModel[]> = signal([]);

  filterFormGroup = new FormGroup({
    searchControl: new FormControl('', { nonNullable: true }),
    employmentStateFilter: new FormControl<Set<EmploymentState>>(new Set([]), { nonNullable: true })
  });

  employmentStateValues: EmploymentState[] = Object.values(EmploymentState);

  // debounceTime(300),
  filterFromGroupValue: Signal<RequiredFormRawValue<typeof this.filterFormGroup>> = toSignal(this.filterFormGroup.valueChanges.pipe(startWith(this.filterFormGroup.getRawValue()), map(() => this.filterFormGroup.getRawValue())), { initialValue: this.filterFormGroup.getRawValue() });

  areAllFiltersActive = computed(() => Array.from(this.filterFromGroupValue().employmentStateFilter).length == 0);

  private readonly service: MemberService = inject(MemberService);

  private readonly scopedTranslationService: ScopedTranslationService = inject(ScopedTranslationService);

  protected columns: GenCol<MemberModel>[] = [
    GenCol.fromAttr('firstName')
      .withLink(),
    GenCol.fromAttr('lastName')
      .withLink(),
    GenCol.fromAttr('birthDate', [(d: DateTime) => d.toLocaleString(DateTime.DATE_MED)]),
    GenCol.fromCalculated('organisationUnit', (e) => e.organisationUnit.name),
    GenCol.fromAttr('employmentState', [(key) => this.scopedTranslationService.instant('EMPLOYMENT_STATUS_VALUES.' + key)])
  ];

  dataSource: GenericTableDataSource<MemberModel> = new GenericTableDataSource<MemberModel>(this.columns, this.members());

  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  private readonly translate = inject(TranslateService);

  constructor() {
    effect((): void => {
      this.dataSource.data = this.members();
    });

    effect((): void => {
      this.writeFormToUrl(this.filterFromGroupValue());
    });

    effect(() => {
      if (this.filterFromGroupValue().employmentStateFilter.size == this.employmentStateValues.length) {
        this.filterFormGroup.patchValue({ employmentStateFilter: new Set() });
      }
    });

    effect(() => {
      const filters = this.filters();
      this.filterFormGroup.patchValue({
        employmentStateFilter: filters.statuses,
        searchControl: filters.searchText
      });
    });
  }

  ngOnInit() {
    this.service.getAllMembers()
      .subscribe((members: MemberModel[]): void => {
        this.members.set(members);
      });
    this.dataSource.filterPredicate = this.createFilterPredicate();
  }

  createFilterPredicate(): (data: MemberModel, filter: string) => boolean {
    return (member: MemberModel, filter: string): boolean => {
      const filterValues = JSON.parse(filter) as RequiredFormRawValue<typeof this.filterFormGroup>;
      const searchTxt: string = filterValues.searchControl.toLowerCase();
      const statuses = Array.from(filterValues.employmentStateFilter);

      const statusMatch: boolean = statuses.length == 0 || statuses.includes(member.employmentState);

      const memberDataString: string = (
        member.firstName +
        member.lastName +
        member.birthDate +
        (member.organisationUnit ? member.organisationUnit.name : this.translate.instant('MEMBER.NO_DIVISION')) +
        this.translate.instant('MEMBER.EMPLOYMENT_STATUS_VALUES.' + member.employmentState)
      ).toLowerCase();

      const searchTerms: string[] = searchTxt.split(' ')
        .filter(Boolean);

      const isTextMatch: boolean = searchTerms.every((term) => memberDataString.includes(term));

      return isTextMatch && statusMatch;
    };
  }

  writeFormToUrl(form: RequiredFormRawValue<typeof this.filterFormGroup>): void {
    const selected: string[] = Array.from(form.employmentStateFilter);
    const formValue = { ...form,
      employmentStateFilter: selected };

    this.dataSource.filter = JSON.stringify(formValue);
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: formValue.searchControl.trim() || null,
        status: selected.length !== 0 ? selected : null
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  toggleFilter(statusFilterValue: EmploymentState): void {
    const newFilters = new Set(this.filterFormGroup.getRawValue().employmentStateFilter);
    if (newFilters.has(statusFilterValue)) {
      newFilters.delete(statusFilterValue);
    } else {
      newFilters.add(statusFilterValue);
    }
    this.filterFormGroup.patchValue({ employmentStateFilter: newFilters });
  }

  isFilterActive(status: EmploymentState): boolean {
    return new Set(this.filterFormGroup.getRawValue().employmentStateFilter)
      .has(status);
  }

  handleAddMemberClick(): void {
    this.router.navigate(['/member/add']);
  }
}
