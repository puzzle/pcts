import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
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
import { MatIcon } from '@angular/material/icon';

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
    MatIcon
  ],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.css'
})
export class MemberFormComponent implements OnInit {
  protected id: number | null = null;

  protected isEdit = false;

  protected statusOptions: EmploymentState[] = [EmploymentState.BEWERBER,
    EmploymentState.EX_MEMBER,
    EmploymentState.MEMBER];

  protected filteredStatusOptions: WritableSignal<EmploymentState[]> = signal([]);

  protected statusSearch = new FormControl('', [Validators.required]);

  protected organisationUnitsOptions: OrganisationUnit[] = [];

  protected filteredOrganisationUnitsOptions: WritableSignal<OrganisationUnit[]> = signal([]);

  protected organisationSearch = new FormControl('', [Validators.required]);

  protected memberService: MemberService = inject(MemberService);

  protected route: ActivatedRoute = inject(ActivatedRoute);

  protected router: Router = inject(Router);

  protected memberForm: FormGroup = new FormGroup({
    firstname: new FormControl('', [Validators.required]),
    lastname: new FormControl('', [Validators.required]),
    abbreviation: new FormControl('', [Validators.required]),
    birthdate: new FormControl('', [Validators.required]),
    employmentDate: new FormControl('', [Validators.required,
      this.isDateInPast]),
    status: new FormControl(null, [Validators.required]),
    organisationUnit: new FormControl(null, [Validators.required])
  });

  ngOnInit() {
    this.memberService.getAllOrganisationUnits()
      .subscribe({
        next: (orgUnits) => this.organisationUnitsOptions = orgUnits,
        error: (error) => console.log('Error loading divisions', error)
      });

    this.organisationSearch.valueChanges.subscribe((value) => {
      if (!value) {
        this.filteredOrganisationUnitsOptions.set(this.organisationUnitsOptions);
      } else {
        this.filteredOrganisationUnitsOptions.set(this.filterOrg(value));
      }
    });

    this.statusSearch.valueChanges.subscribe((value) => {
      if (!value) {
        this.filteredStatusOptions.set(this.statusOptions);
      } else {
        this.filteredStatusOptions.set(this.filterStatus(value));
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
            this.memberForm.controls['status'].setValue(member.employment_state);

            const divisionObject = this.organisationUnitsOptions.find((d) => d === member.organisation_unit);
            this.memberForm.controls['division'].setValue(divisionObject);
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
      employment_state: this.memberForm.get('status')?.value,
      date_of_hire: this.memberForm.get('employmentDate')?.value,
      is_admin: false,
      organisation_unit: this.memberForm.get('division')?.value
    });
    this.router.navigate(['']);
  }

  onCancel() {
    this.router.navigate(['']);
  }


  isDateInPast(control: AbstractControl): ValidationErrors | null {
    const inputDate = new Date(control.value);
    const today = new Date();

    if (isNaN(inputDate.getTime())) {
      return null;
    }

    return inputDate < today ? null : { dateNotInPast: true };
  }

  private filterOrg(value: string): OrganisationUnit[] {
    const filterValue = value.toLowerCase();

    return this.organisationUnitsOptions.filter((option) => option.name.toLowerCase()
      .includes(filterValue));
  }

  private filterStatus(value: string): EmploymentState[] {
    const filterValue = value.toLowerCase();

    return this.statusOptions.filter((option) => option.toLowerCase()
      .includes(filterValue));
  }
}
