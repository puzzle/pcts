import { Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { BaseModalComponent } from '../../../shared/modal/base-modal.component';
import { InputFieldComponent } from '../../../shared/input-field/input-field.component';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';
import { MatError, MatFormField, MatInput, MatLabel, MatSuffix } from '@angular/material/input';
import { PctsFormErrorDirective } from '../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../shared/pcts-form-label/pcts-form-label.directive';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { DialogResult, StrictlyTypedDialog } from '../../../shared/modal/strictly-typed-dialog.helper';
import { MemberModel } from '../../member/member.model';
import { isInteger, isValueInListSignal } from '../../../shared/form/form-validators';
import { toSignal } from '@angular/core/rxjs-interop';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { ExperienceModel } from '../experience.model';
import { ExperienceTypeModel } from '../experience-type/experience-type.model';
import { MatCheckbox } from '@angular/material/checkbox';
import { provideI18nPrefix } from '../../../shared/i18n-prefix.provider';
import { ExperienceTypeService } from '../experience-type/experience-type.service';
import { ModalActionsComponent } from '../../../shared/modal/modal-actions.component';

@Component({
  selector: 'app-add-experience',
  imports: [
    BaseFormComponent,
    BaseModalComponent,
    InputFieldComponent,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSuffix,
    PctsFormErrorDirective,
    PctsFormLabelDirective,
    ReactiveFormsModule,
    ScopedTranslationPipe,
    MatCheckbox,
    ModalActionsComponent
  ],

  templateUrl: './add-experience.component.html',
  providers: [provideI18nPrefix('EXPERIENCE.FORM.ADD')]

})
export class AddExperienceComponent extends StrictlyTypedDialog<ExperienceModel | undefined, DialogResult<ExperienceModel>> implements OnInit {
  private readonly fb = inject(FormBuilder);

  private readonly experienceTypeService = inject(ExperienceTypeService);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly experienceTypeOptions: WritableSignal<ExperienceTypeModel[]> = signal([]);

  formGroup = this.fb.nonNullable.group({
    id: [null as null | number],
    name: ['' as string | null,
      Validators.required],
    member: [null as MemberModel | null],
    experienceType: [null as ExperienceTypeModel | null,
      [Validators.required,
        isValueInListSignal(this.experienceTypeOptions, (a, b) => a.id === b.id)]],
    employer: ['' as string | null],
    percent: [null as number | null,
      [
        Validators.required,
        Validators.min(0),
        Validators.max(120),
        isInteger()
      ]],
    endDate: [null as Date | null],
    startDate: [null as Date | null,
      Validators.required],
    comment: ['' as string | null]
  });

  constructor() {
    super();
    if (this.data) {
      this.formGroup.patchValue({
        ...this.data
      });
    }
  }

  ngOnInit(): void {
    this.experienceTypeService.getAllExperienceTypes()
      .subscribe((experienceTypes) => {
        this.experienceTypeOptions.set(experienceTypes);
        this.formGroup.get('experienceType')
          ?.updateValueAndValidity();
      });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected displayExperienceTypes = (experienceType: ExperienceTypeModel | null | undefined): string => {
    return experienceType?.name ?? '';
  };

  protected experienceTypeControlSignal = toSignal(this.formGroup.get('experienceType')!.valueChanges, {
    initialValue: this.formGroup.get('experienceType')!.value
  });

  protected experienceTypeFilteredOptions = computed(() => {
    const value = this.experienceTypeControlSignal() ?? '';
    return this.filterExperienceType(value);
  });

  filterExperienceType(value: ExperienceTypeModel | string | null): ExperienceTypeModel[] {
    if (value === null || value === undefined || value === '') {
      return this.experienceTypeOptions();
    }

    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    if (filterValue === '') {
      return this.experienceTypeOptions();
    }
    return this.experienceTypeOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }

  onSubmit(submitMod: ModalSubmitMode) {
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue() as ExperienceModel
    });
  }
}


