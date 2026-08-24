import { Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { BaseModalComponent } from '../../../shared/modal/base-modal.component';
import { InputFieldComponent } from '../../../shared/input-field/input-field.component';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';
import { MatError, MatFormField, MatInput, MatLabel, MatSuffix } from '@angular/material/input';
import { MenuButtonComponent } from '../../../shared/menu-button/menu-button.component';
import { PctsFormErrorDirective } from '../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../shared/pcts-form-label/pcts-form-label.directive';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { DialogResult, StrictlyTypedDialog } from '../../../shared/modal/strictly-typed-dialog.helper';
import { MemberModel } from '../../member/member.model';
import { isValueInListSignal } from '../../../shared/form/form-validators';
import { toSignal } from '@angular/core/rxjs-interop';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { ExperienceModel } from '../experience.model';
import { ExperienceTypeModel } from '../experience-type/experience-type.model';
import { MatCheckbox } from '@angular/material/checkbox';
import { provideI18nPrefix } from '../../../shared/i18n-prefix.provider';
import { ExperienceTypeService } from '../experience-type/experience-type.service';

@Component({
  selector: 'app-add-experience',
  imports: [
    BaseFormComponent,
    BaseModalComponent,
    InputFieldComponent,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatButton,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSuffix,
    MenuButtonComponent,
    PctsFormErrorDirective,
    PctsFormLabelDirective,
    ReactiveFormsModule,
    ScopedTranslationPipe,
    MatCheckbox
  ],

  templateUrl: './add-experience.component.html',
  providers: [provideI18nPrefix('EXPERIENCE.FORM.ADD')],
  styleUrl: './add-experience.component.scss'
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
      Validators.required],
    endDate: [null as Date | null],
    completed: [true as boolean | null],
    startDate: [null as Date | null,
      Validators.required],
    comment: ['' as string | null]
  });

  constructor() {
    super();
  }

  ngOnInit(): void {
    this.formGroup.patchValue(this.data ?? {});
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
    const model = this.experienceTypeControlSignal();
    const value = model?.name ?? '';

    return this.filterExperienceType(value, this.experienceTypeOptions());
  });

  filterExperienceType(value: string, experienceTypeOptions: ExperienceTypeModel[]): ExperienceTypeModel[] {
    if (!value) {
      return experienceTypeOptions;
    }

    return experienceTypeOptions
      .filter((option) => option.name.toLowerCase()
        .includes(value.toLowerCase()));
  }

  onSubmit(submitMod: ModalSubmitMode) {
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue() as ExperienceModel
    });
  }
}


