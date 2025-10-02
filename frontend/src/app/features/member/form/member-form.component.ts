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
import { ActivatedRoute, Router } from '@angular/router';
import { EmploymentState } from '../member.model';
import { OrganisationUnit } from '../../organisation-unit/organisation-unit.model';
import { MemberService } from '../member.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { TranslatePipe } from '@ngx-translate/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { ErrorComponent } from '../../../shared/error/error.component';

import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDateFormats } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';

const MY_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: 'DD.MM.yyyy'
  },
  display: {
    dateInput: 'DD.MM.yyyy',
    monthYearLabel: 'MMM yyyy',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM yyyy'
  }
};

@Component({
  selector: 'app-member-form.component',
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
    ErrorComponent
  ],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.scss',
  providers: [{ provide: DateAdapter,
    useClass: MomentDateAdapter,
    deps: [MAT_DATE_LOCALE] },
  { provide: MAT_DATE_FORMATS,
    useValue: MY_DATE_FORMATS }]
})
export class MemberFormComponent implements OnInit {
  protected id: number | null = null;

  protected isEdit = false;

  protected employmentStateOptions: EmploymentState[] = Object.values(EmploymentState);

  protected organisationUnitsOptions: OrganisationUnit[] = [];

  protected memberService: MemberService = inject(MemberService);

  protected route: ActivatedRoute = inject(ActivatedRoute);

  protected router: Router = inject(Router);

  protected memberForm: FormGroup = new FormGroup({
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    nickname: new FormControl(''),
    birthdate: new FormControl('', [Validators.required,
      this.validateDate()]),
    employmentDate: new FormControl(''),
    employmentState: new FormControl(null, [Validators.required]),
    organisationUnit: new FormControl(null)
  });

  protected filteredEmploymentStateOptions = computed(() => {
    const value = this.memberForm.get('employmentState')?.value;
    return value ? this.filterEmploymentState(value) : this.employmentStateOptions;
  });

  protected filteredOrganisationUnitsOptions = computed(() => {
    const value = this.memberForm.get('organisationUnit')?.value;
    return value ? this.filterOrg(value) : this.organisationUnitsOptions;
  });

  ngOnInit() {
    this.memberService.getAllOrganisationUnits()
      .subscribe({
        next: (orgUnits) => this.organisationUnitsOptions = orgUnits,
        error: (error) => console.error('Error loading organisation units', error)
      });

    const id: number | null = this.route.snapshot.paramMap.get('id') ? Number(this.route.snapshot.paramMap.get('id')) : null;
    this.loadMember(id!);
  }

  loadMember(id: number) {
    if (id) {
      this.memberService.getMemberById(id)
        .subscribe({
          next: (member) => {
            this.memberForm.controls['firstname'].setValue(member.name);
            this.memberForm.controls['lastname'].setValue(member.last_name);
            this.memberForm.controls['nickname'].setValue(member.nickname);
            this.memberForm.controls['birthdate'].setValue(member.birthday);
            this.memberForm.controls['employmentDate'].setValue(member.date_of_hire);
            this.memberForm.controls['employmentState'].setValue(member.employment_state);

            const organisationUnit = this.organisationUnitsOptions.find((d) => d.name === member.organisation_unit.name);
            this.memberForm.controls['organisationUnit'].setValue(organisationUnit?.name);
          }
        });
    }
  }

  onSubmit() {
    const formValues = this.memberForm.value;

    const newMember = {
      id: 0,
      name: formValues.firstname,
      last_name: formValues.lastname,
      birthday: formValues.birthdate,
      nickname: formValues.nickname,
      employment_state: formValues.employmentState,
      date_of_hire: formValues.employmentDate,
      is_admin: false,
      organisation_unit: formValues.organisationUnit
    };

    this.memberService.addMember(newMember);
    this.router.navigate(['']);
  }

  onCancel() {
    this.router.navigate(['']);
  }

  private filterOrg(value: string): OrganisationUnit[] {
    const filterValue = value.toLowerCase();

    return this.organisationUnitsOptions.filter((option) => option.name.toLowerCase()
      .includes(filterValue));
  }

  private filterEmploymentState(value: string): EmploymentState[] {
    const filterValue = value.toLowerCase();

    return this.employmentStateOptions.filter((option) => option.toLowerCase()
      .includes(filterValue));
  }

  validateDate(): ValidatorFn {
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
}
