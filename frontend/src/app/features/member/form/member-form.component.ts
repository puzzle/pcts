import {Component, computed, inject, OnInit, Signal, signal} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {MemberService} from '../member.service';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatButton} from '@angular/material/button';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import {MatIconModule} from '@angular/material/icon';
import {MatDatepickerModule} from '@angular/material/datepicker';

import {OrganisationUnitModel} from '../../organisation-unit/organisation-unit.model';
import {EmploymentState} from '../../../shared/enum/employment-state.enum';
import {toSignal} from '@angular/core/rxjs-interop';
import {MemberModel} from '../member.model';
import {OrganisationUnitService} from '../../organisation-unit/organisation-unit.service';
import {InputField} from '../../../shared/input-field/input-field';
import {InputTypeEnum} from '../../../shared/input-field/input-type.enum';

@Component({
  selector: 'app-member-form',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatButton,
    TranslatePipe,
    MatIconModule,
    MatDatepickerModule,
    RouterLink,
    InputField,
  ],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.scss'
})
export class MemberFormComponent implements OnInit {
  private readonly translateService = inject(TranslateService);

  private readonly memberService = inject(MemberService);

  private readonly organisationUnitService = inject(OrganisationUnitService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  private readonly fb = inject(FormBuilder);

  protected readonly InputTypeEnum = InputTypeEnum;

  protected isEdit = signal<boolean>(false);

  protected pageTitleKey: Signal<string> = computed(() => (this.isEdit() ? 'GENERAL.EDIT' : 'GENERAL.ADD'));

  protected submitButtonKey: Signal<string> = computed(() => (this.isEdit() ? 'GENERAL.SAVE' : 'GENERAL.ADD'));

  protected memberForm: FormGroup = this.fb.group({
    id: [null],
    name: ['',
      Validators.required],
    lastName: ['',
      Validators.required],
    abbreviation: [''],
    birthday: ['',
      [Validators.required,
        this.isDateInPast()]],
    dateOfHire: [''],
    employmentState: [null,
      [Validators.required,
        this.isAEmploymentState()]],
    organisationUnit: [null,
      this.isAOrganisationUnit()]
  });

  private readonly employmentStateOptions: EmploymentState[] = Object.values(EmploymentState);

  protected employmentStateControlSignal = toSignal(this.memberForm.get('employmentState')!.valueChanges, { initialValue: this.memberForm.get('employmentState')!.value });

  protected employmentStateFilteredOptions = computed(() => {
    const value = this.employmentStateControlSignal() || '';
    return this.filterEmploymentState(value);
  });

  private readonly organisationUnitsOptions = toSignal(this.organisationUnitService.getAllOrganisationUnits(), { initialValue: [] });

  protected organisationUnitControlSignal = toSignal(this.memberForm.get('organisationUnit')!.valueChanges, { initialValue: this.memberForm.get('organisationUnit')!.value });

  protected organisationUnitFilteredOptions = computed(() => {
    const value = this.organisationUnitControlSignal();
    return this.filterOrganisationUnit(value);
  });

  ngOnInit(): void {
    //wait until the i18n translation json is loaded
    this.translateService.onLangChange.subscribe(() => {
      this.loadMember();
    });
  }

  loadMember() {
    const member = this.route.snapshot.data['memberData'] as MemberModel;
    if (member?.id) {
      this.isEdit.set(true);
    }
    this.memberForm.patchValue(member);
  }

  onSubmit() {
    this.memberForm.markAllAsTouched();
    this.memberForm.markAllAsDirty();
    if (this.memberForm.invalid) {
      return;
    }

    const memberToSave = this.memberForm.getRawValue() as MemberModel;

    if (this.isEdit()) {
      this.memberService.updateMember(this.memberForm.get('id')?.value, memberToSave);
    } else {
      memberToSave.id = 0;
      this.memberService.addMember(memberToSave);
    }

    this.router.navigate(['/']);
  }

  isDateInPast(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null;
      }

      const date = new Date(control.value);
      const today = new Date();

      console.log(date)

      if (date >= today) {
        return { date_is_in_future: true };
      }

      return null;
    };
  }

  isAOrganisationUnit(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value) {
        return null;
      }

      if (typeof value === 'string') {
        return {invalid_entry: true};
      }

      const isValidOption = this.organisationUnitsOptions()
        .includes(value);

      return isValidOption ? null : {invalid_entry: true};
    };
  }

  isAEmploymentState(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;

      if (!value) {
        return null;
      }

      if (value === EmploymentState.EX_MEMBER || value === EmploymentState.APPLICANT || value === EmploymentState.MEMBER) {
        return null;
      }
      return { invalid_entry: true };
    };
  }

  protected displayEmploymentState(employmentState: EmploymentState | string): string {
    if (!employmentState) {
      return '';
    }
    const translationKey = 'MEMBER.EMPLOYMENT_STATUS_VALUES.' + employmentState;
    return this.translateService.instant(translationKey);
  }

  protected displayOrganisationUnit(organisationUnit: OrganisationUnitModel): string {
    if (!organisationUnit) {
      return '';
    }

    return organisationUnit.name;
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
