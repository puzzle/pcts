import { Component, computed, inject, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MemberService } from '../member.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';

import { OrganisationUnitModel } from '../../organisation-unit/organisation-unit.model';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { toSignal } from '@angular/core/rxjs-interop';
import { ErrorComponent } from '../../../shared/error/error.component';
import { MemberModel } from '../member.model';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';

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
    ErrorComponent,
    RouterLink
  ],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.scss'
})
export class MemberFormComponent implements OnInit {
  protected isEdit = false;

  private id = 0;

  protected memberForm: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    abbreviation: new FormControl(''),
    birthday: new FormControl('', [Validators.required,
      this.isDateInPast()]),
    dateOfHire: new FormControl(''),
    employmentState: new FormControl(null, [Validators.required]),
    organisationUnit: new FormControl(null)
  });

  private readonly employmentStateOptions: EmploymentState[] = Object.values(EmploymentState);

  protected employmentStateControlSignal = toSignal(this.memberForm.get('employmentState')!.valueChanges, { initialValue: this.memberForm.get('employmentState')!.value });

  protected employmentStateFilteredOptions = computed(() => {
    const value = this.employmentStateControlSignal() || '';
    return this.filterEmploymentState(value);
  });

  private organisationUnitsOptions: OrganisationUnitModel[] = [];

  protected organisationUnitControlSignal = toSignal(this.memberForm.get('organisationUnit')!.valueChanges, { initialValue: this.memberForm.get('organisationUnit')!.value });

  protected organisationUnitFilteredOptions = computed(() => {
    const value = this.organisationUnitControlSignal() || '';
    return this.filterOrganisationUnit(value);
  });

  private readonly translateService = inject(TranslateService);

  private readonly memberService = inject(MemberService);

  private readonly organisationUnitService = inject(OrganisationUnitService);

  private readonly route = inject(ActivatedRoute);

  private readonly router = inject(Router);

  ngOnInit() {
    this.organisationUnitService.getAllOrganisationUnits()
      .subscribe({
        next: (orgUnits) => this.organisationUnitsOptions = orgUnits,
        error: (error) => console.error('Error loading organisation units', error)
      });

    this.id = this.route.snapshot.paramMap.get('id') ? Number(this.route.snapshot.paramMap.get('id')) : 0;
    this.isEdit = this.id !== 0;

    if (this.isEdit) {
      this.loadMember();
    }
  }

  loadMember() {
    const { name, lastname, abbreviation, birthday, dateOfHire, employmentState, organisationUnit } = this.memberForm.controls;

    this.memberService.getMemberById(this.id)
      .subscribe((member) => {
        this.translateService.get('MEMBER.EMPLOYMENT_STATUS_VALUES.' + member.employmentState)
          .subscribe(() => {
            employmentState.setValue(member.employmentState);
          });

        name.setValue(member.name);
        lastname.setValue(member.lastName);
        abbreviation.setValue(member.abbreviation);
        birthday.setValue(member.birthday);
        dateOfHire.setValue(member.dateOfHire);
        organisationUnit.setValue(member.organisationUnit);
      });
  }

  onSubmit() {
    const memberToSave: MemberModel = {
      ...this.memberForm.value,
      id: 0
    } as MemberModel;

    if (this.isEdit) {
      this.memberService.updateMember(this.id, memberToSave);
    } else {
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

      if (date >= today) {
        return { date_is_in_future: true };
      }

      return null;
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

  private filterOrganisationUnit(value: OrganisationUnitModel | string): OrganisationUnitModel[] {
    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    return this.organisationUnitsOptions.filter((option) => option.name.toLowerCase()
      .includes(filterValue));
  }
}
