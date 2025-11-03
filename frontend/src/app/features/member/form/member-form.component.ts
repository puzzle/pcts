import { Component, computed, effect, inject, input, OnInit, signal, WritableSignal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { OrganisationUnitModel } from '../../organisation-unit/organisation-unit.model';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { toSignal } from '@angular/core/rxjs-interop';
import { MemberModel } from '../member.model';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';
import { PctsFormErrorDirective } from '../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../shared/pcts-form-label/pcts-form-label.directive';
import { InputFieldComponent } from '../../../shared/input-field/input-field.component';
import { map } from 'rxjs';
import { isDateInFuture, isValueInList, isValueInListSignal } from '../../../shared/form/form-validators';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { DateTime } from 'luxon';

@Component({
  selector: 'app-member-form',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    TranslatePipe,
    MatIconModule,
    MatDatepickerModule,
    PctsFormErrorDirective,
    PctsFormLabelDirective,
    InputFieldComponent,
    BaseFormComponent
  ],
  templateUrl: './member-form.component.html'
})
export class MemberFormComponent implements OnInit {
  private readonly translateService = inject(TranslateService);

  private readonly memberService = inject(MemberService);

  private readonly organisationUnitService = inject(OrganisationUnitService);

  private readonly router = inject(Router);

  private readonly fb = inject(FormBuilder);

  readonly member = input.required<MemberModel>();

  protected isEdit = computed(() => {
    return !!this.member();
  });

  private readonly employmentStateOptions: EmploymentState[] = Object.values(EmploymentState);

  private readonly organisationUnitsOptions: WritableSignal<OrganisationUnitModel[]> = signal([]);

  protected memberForm: FormGroup = this.fb.group({
    id: [null],
    firstName: ['',
      Validators.required],
    lastName: ['',
      Validators.required],
    abbreviation: [''],
    birthDate: ['',
      [Validators.required,
        isDateInFuture()]],
    dateOfHire: [''],
    employmentState: [null,
      [Validators.required,
        isValueInList(this.employmentStateOptions)]],
    organisationUnit: [null,
      isValueInListSignal(this.organisationUnitsOptions)]
  });

  protected employmentStateControlSignal = toSignal(this.memberForm.get('employmentState')!.valueChanges.pipe(map((value) => value ?? '')), {
    initialValue: this.memberForm.get('employmentState')!.value ?? ''
  });

  protected employmentStateFilteredOptions = computed(() => {
    const value = this.employmentStateControlSignal() ?? '';
    return this.filterEmploymentState(value);
  });

  protected organisationUnitControlSignal = toSignal(this.memberForm.get('organisationUnit')!.valueChanges, { initialValue: this.memberForm.get('organisationUnit')!.value });

  protected organisationUnitFilteredOptions = computed(() => {
    const value = this.organisationUnitControlSignal();
    return this.filterOrganisationUnit(value);
  });

  ngOnInit() {
    this.organisationUnitService.getAllOrganisationUnits()
      .subscribe((organisationUnits) => {
        this.organisationUnitsOptions.set(organisationUnits);
        this.memberForm.get('organisationUnit')
          ?.updateValueAndValidity();
      });
  }

  constructor() {
    effect(() => {
      if (!this.member()) {
        return;
      }
      this.memberForm.patchValue({
        ...this.member(),
        birthDate: DateTime.fromISO(this.member().birthDate.toString()),
        dateOfHire: DateTime.fromISO(this.member().dateOfHire.toString())
      });

      this.memberForm.get('organisationUnit')
        ?.setValue(this.organisationUnitsOptions()
          .find((orgUnit) => orgUnit.id === this.member().organisationUnit.id));
    });
  }

  onSubmit() {
    if (this.memberForm.invalid) {
      return;
    }
    const memberToSave = this.memberForm.getRawValue() as MemberModel;
    if (this.isEdit()) {
      this.memberService.updateMember(this.memberForm.get('id')?.value, memberToSave)
        .subscribe(() => {
          this.router.navigate(['/']);
        });
    } else {
      this.memberService.addMember(memberToSave)
        .subscribe(() => {
          this.router.navigate(['/']);
        });
    }
  }

  onCancel() {
    this.router.navigate(['/']);
  }

  protected displayEmploymentState = (employmentState: EmploymentState | string): string => {
    if (!employmentState) {
      return '';
    }
    const translationKey = 'MEMBER.EMPLOYMENT_STATUS_VALUES.' + employmentState;
    return this.translateService.instant(translationKey);
  };

  protected displayOrganisationUnit(organisationUnit: OrganisationUnitModel): string {
    if (!organisationUnit) {
      return '';
    }
    return organisationUnit?.name ?? '';
  }

  protected filterEmploymentState(value: string): EmploymentState[] {
    const filterValue = value?.toLowerCase() || '';

    return this.employmentStateOptions.filter((option) => {
      const translationKey = 'MEMBER.EMPLOYMENT_STATUS_VALUES.' + option;
      const translatedValue = this.translateService.instant(translationKey);
      return translatedValue.toLowerCase()
        .includes(filterValue);
    });
  }

  private filterOrganisationUnit(value: OrganisationUnitModel | string | null): OrganisationUnitModel[] {
    if (value === null || value === undefined || value === '') {
      return this.organisationUnitsOptions();
    }


    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    if (filterValue === '') {
      return this.organisationUnitsOptions();
    }
    return this.organisationUnitsOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }
}
