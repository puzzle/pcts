import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors, ValidatorFn,
  Validators
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ActivatedRoute, Router } from '@angular/router';
import { EmploymentState } from '../dto/member.model';
import { OrganisationUnit } from '../dto/organisation-unit.model';
import { MemberService } from '../member.service';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { TranslatePipe } from '@ngx-translate/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';

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
    MatDatepickerModule
  ],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.css'
})
export class MemberFormComponent implements OnInit {
  protected id: number | null = null;

  protected isEdit = false;

  protected employmentStateOptions: EmploymentState[] = [EmploymentState.BEWERBER,
    EmploymentState.EX_MEMBER,
    EmploymentState.MEMBER];

  protected filteredEmploymentStateOptions: WritableSignal<EmploymentState[]> = signal([]);

  protected organisationUnitsOptions: OrganisationUnit[] = [];

  protected filteredOrganisationUnitsOptions: WritableSignal<OrganisationUnit[]> = signal([]);


  protected memberService: MemberService = inject(MemberService);

  protected route: ActivatedRoute = inject(ActivatedRoute);

  protected router: Router = inject(Router);

  protected memberForm: FormGroup = new FormGroup({
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    abbreviation: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required,
      validateDate(ValidationType.PAST)]),
    employmentDate: new FormControl('', [Validators.required]),
    employmentState: new FormControl(null, [Validators.required]),
    organisationUnit: new FormControl(null, [Validators.required])
  });

  ngOnInit() {
    this.memberService.getAllOrganisationUnits()
      .subscribe({
        next: (orgUnits) => {
          this.organisationUnitsOptions = orgUnits;
          this.filteredOrganisationUnitsOptions.set(this.organisationUnitsOptions);
        },
        error: (error) => console.log('Error loading organisation units', error)
      });

    this.filteredEmploymentStateOptions.set(this.employmentStateOptions);

    this.organisationUnit.valueChanges.subscribe((value) => {
      if (!value) {
        this.filteredOrganisationUnitsOptions.set(this.organisationUnitsOptions);
      } else {
        this.filteredOrganisationUnitsOptions.set(this.filterOrg(value));
      }
    });

    this.employmentState.valueChanges.subscribe((value) => {
      if (!value) {
        this.filteredEmploymentStateOptions.set(this.employmentStateOptions);
      } else {
        this.filteredEmploymentStateOptions.set(this.filterEmploymentState(value));
      }
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEdit = true;
      this.loadMember(+idParam);
    } else {
      this.isEdit = false;
    }
  }

  loadMember(id: number) {
    if (id) {
      this.memberService.getMemberById(id)
        .subscribe({
          next: (member) => {
            this.memberForm.controls['firstname'].setValue(member.name);
            this.memberForm.controls['lastname'].setValue(member.last_name);
            this.memberForm.controls['abbreviation'].setValue(member.abbreviation);
            this.memberForm.controls['birthdate'].setValue(member.birthday.toISOString()
              .substring(0, 10));
            this.memberForm.controls['employmentDate'].setValue(member.date_of_hire.toISOString()
              .substring(0, 10));
            this.memberForm.controls['employmentState'].setValue(member.employment_state);

            const organisationUnit = this.organisationUnitsOptions.find((d) => d.name === member.organisation_unit.name);
            this.memberForm.controls['organisationUnit'].setValue(organisationUnit?.name);
          }
        });
    }
  }

  onSubmit() {
    this.memberService.addMember({
      id: 0,
      name: this.memberForm.get('firstname')?.value,
      last_name: this.memberForm.get('lastname')?.value,
      birthday: this.memberForm.get('birthdate')?.value,
      abbreviation: this.memberForm.get('abbreviation')?.value,
      employment_state: this.memberForm.get('employmentState')?.value,
      date_of_hire: this.memberForm.get('employmentDate')?.value,
      is_admin: false,
      organisation_unit: this.memberForm.get('organisationUnit')?.value
    });
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

  get firstname(): FormControl {
    return this.memberForm.get('firstname') as FormControl;
  }

  get lastname(): FormControl {
    return this.memberForm.get('lastname') as FormControl;
  }

  get abbreviation(): FormControl {
    return this.memberForm.get('abbreviation') as FormControl;
  }

  get birthday(): FormControl {
    return this.memberForm.get('birthdate') as FormControl;
  }

  get employmentDate(): FormControl {
    return this.memberForm.get('employmentDate') as FormControl;
  }

  get employmentState(): FormControl {
    return this.memberForm.get('employmentState') as FormControl;
  }

  get organisationUnit(): FormControl {
    return this.memberForm.get('organisationUnit') as FormControl;
  }
}

export enum ValidationType {
  PAST = 'past',
  FUTURE = 'future'
}

export function validateDate(type: ValidationType): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }

    const date = new Date(control.value);
    const today = new Date();

    if (type === ValidationType.PAST) {
      if (date >= today) {
        return { invalidDate: true };
      }
    } else if (type === ValidationType.FUTURE) {
      if (date <= today) {
        return { invalidDate: true };
      }
    }

    return null;
  };
}
