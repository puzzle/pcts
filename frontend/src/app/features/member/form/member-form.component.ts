import {
  Component,
  computed,
  effect,
  inject,
  input,
  model,
  ModelSignal,
  OnInit,
  signal,
  WritableSignal
} from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../member.service';
import { MatAutocompleteModule, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { TranslateService } from '@ngx-translate/core';
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
import { isDateInPast, isValueInList, isValueInListSignal } from '../../../shared/form/form-validators';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { Location } from '@angular/common';
import { RoleModel } from '../../roles/RoleModel';
import { RoleService } from '../../roles/role.service';
import { MatChipGrid, MatChipInput, MatChipRemove, MatChipRow } from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-member-form',
  imports: [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatIconModule,
    MatDatepickerModule,
    PctsFormErrorDirective,
    PctsFormLabelDirective,
    InputFieldComponent,
    BaseFormComponent,
    ScopedTranslationPipe,
    MatChipGrid,
    MatChipRow,
    MatChipInput,
    MatChipRemove
  ],
  templateUrl: './member-form.component.html'
})
export class MemberFormComponent implements OnInit {
  private readonly translateService = inject(TranslateService);

  private readonly memberService = inject(MemberService);

  private readonly roleService = inject(RoleService);

  private readonly organisationUnitService = inject(OrganisationUnitService);

  private readonly router = inject(Router);

  private readonly fb = inject(FormBuilder);

  private readonly location = inject(Location);

  readonly member = input.required<MemberModel>();

  protected isEdit = computed(() => {
    return !!this.member();
  });

  private readonly employmentStateOptions: string[] = Object.values(EmploymentState);

  private readonly roleOptions: WritableSignal<RoleModel[]> = signal([]);

  private readonly organisationUnitsOptions: WritableSignal<OrganisationUnitModel[]> = signal([]);

  readonly separatorKeysCodes: number[] = [ENTER,
    COMMA];

  readonly roles: WritableSignal<RoleModel[]> = signal([]);

  readonly currentRole: ModelSignal<RoleModel | undefined> = model();

  roleSearchControl = new FormControl('');

  private readonly activatedRoute = inject(ActivatedRoute);

  protected memberForm: FormGroup = this.fb.group({
    id: [null],
    firstName: ['',
      Validators.required],
    lastName: ['',
      Validators.required],
    abbreviation: [''],
    birthDate: ['',
      [Validators.required,
        isDateInPast()]],
    dateOfHire: [''],
    employmentState: [null,
      [Validators.required,
        isValueInList(this.employmentStateOptions, (a, b) => a == b)]],
    roles: [[] as RoleModel[]],
    organisationUnit: [null,
      isValueInListSignal(this.organisationUnitsOptions, (a, b) => a.id === b.id)]
  });

  protected employmentStateControlSignal = toSignal(this.memberForm.get('employmentState')!.valueChanges.pipe(map((value) => value ?? '')), {
    initialValue: this.memberForm.get('employmentState')!.value ?? ''
  });

  protected employmentStateFilteredOptions = computed(() => {
    const value = this.employmentStateControlSignal() ?? '';
    return this.filterEmploymentState(value);
  });

  protected roleFilteredOptions: RoleModel[] = [];

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

    this.roleService.getAllRoles()
      .subscribe((roles) => {
        this.roleOptions.set(roles);
        this.memberForm.get('roles')
          ?.updateValueAndValidity();
      });

    if (this.isEdit()) {
      this.roles.set(this.member().roles);
    }
  }

  constructor() {
    effect(() => {
      if (!this.member()) {
        return;
      }
      this.memberForm.patchValue({
        ...this.member()
      });

      this.memberForm.get('organisationUnit')
        ?.setValue(this.organisationUnitsOptions()
          .find((orgUnit) => orgUnit.id === this.member()?.organisationUnit?.id));
    });
    this.roleSearchControl.valueChanges.subscribe((searchText) => {
      this.roleFilteredOptions = this.filterRole(searchText);
    });
  }

  onSubmit() {
    if (this.memberForm.invalid) {
      return;
    }

    const formData = this.memberForm.getRawValue() as MemberModel;

    const memberToSave = { ...formData,
      roles: this.roles() };

    if (this.isEdit()) {
      this.memberService.updateMember(this.memberForm.get('id')?.value, memberToSave)
        .subscribe(() => {
          this.router.navigate(['/member',
            this.memberForm.getRawValue().id]);
        });
    } else {
      this.memberService.addMember(memberToSave)
        .subscribe(() => {
          this.router.navigate(['/']);
        });
    }
  }

  onCancel() {
    this.location.back();
  }

  protected displayEmploymentState = (employmentState: EmploymentState | string): string => {
    if (!employmentState) {
      return '';
    }
    const translationKey = 'MEMBER.EMPLOYMENT_STATUS_VALUES.' + employmentState;
    return this.translateService.instant(translationKey);
  };

  protected formatRoleName(role: RoleModel): string {
    return role?.name ?? '';
  }

  protected displayOrganisationUnit(organisationUnit: OrganisationUnitModel): string {
    if (!organisationUnit) {
      return '';
    }
    return organisationUnit?.name ?? '';
  }

  protected filterEmploymentState(value: string): string[] {
    const filterValue = value?.toLowerCase() || '';

    return this.employmentStateOptions.filter((option) => {
      const translationKey = 'MEMBER.EMPLOYMENT_STATUS_VALUES.' + option;
      const translatedValue = this.translateService.instant(translationKey);
      return translatedValue.toLowerCase()
        .includes(filterValue);
    });
  }

  protected filterRole(value: RoleModel | string | null): RoleModel[] {
    if (!value) {
      return this.roleOptions();
    }

    const filterValue = (typeof value === 'string' ? value : value?.name)?.toLowerCase();

    if (filterValue === '') {
      return this.roleOptions();
    }
    return this.roleOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }

  private filterOrganisationUnit(value: OrganisationUnitModel | string | null): OrganisationUnitModel[] {
    if (!value) {
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

  removeRole(roleToRemove: RoleModel): void {
    this.roles.update((roles) => {
      return roles.filter((role) => role !== roleToRemove);
    });
  }

  selectRole(event: MatAutocompleteSelectedEvent): void {
    const choosenRole: RoleModel = event.option.value;

    if (!choosenRole) {
      this.currentRole.set(undefined);
      event.option.deselect();
      return;
    }

    this.roles.update((roles) => [...roles,
      choosenRole]);
    this.memberForm.get('roles')
      ?.setValue(this.roles());
    this.currentRole.set(undefined);
    event.option.deselect();
  }
}
