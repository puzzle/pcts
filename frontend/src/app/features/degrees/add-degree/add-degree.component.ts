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
import { DegreeModel } from '../degree.model';
import { DegreeTypeModel } from '../degree-type/degree-type.model';
import { DegreeTypeService } from '../degree-type/degree-type.service';
import { MatCheckbox } from '@angular/material/checkbox';
import { provideI18nPrefix } from '../../../shared/i18n-prefix.provider';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'app-add-degree',
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
    MatCheckbox,
    TranslatePipe
  ],
  templateUrl: './add-degree.component.html',
  providers: [provideI18nPrefix('DEGREE.FORM.ADD')],
  styleUrl: './add-degree.component.scss'
})
export class AddDegreeComponent extends StrictlyTypedDialog<DegreeModel | undefined, DialogResult<DegreeModel>> implements OnInit {
  private readonly fb = inject(FormBuilder);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly degreeTypeOptions: WritableSignal<DegreeTypeModel[]> = signal([]);

  private readonly degreeTypeService = inject(DegreeTypeService);

  formGroup = this.fb.nonNullable.group({
    id: [null as null | number],
    name: ['' as string | null,
      Validators.required],
    member: [null as MemberModel | null],
    type: [null as DegreeTypeModel | null,
      [Validators.required,
        isValueInListSignal(this.degreeTypeOptions, (a, b) => a.id === b.id)]],
    institution: ['' as string | null,
      Validators.required],
    completed: [true as boolean | null],
    endDate: [null as Date | null,
      Validators.required],
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
    this.degreeTypeService.getAllDegreeTypes()
      .subscribe((organisationUnits) => {
        this.degreeTypeOptions.set(organisationUnits);
        this.formGroup.get('degreeType')
          ?.updateValueAndValidity();
      });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected displayDegreeTypes = (degreeType: DegreeTypeModel): string => {
    return degreeType?.name ?? '';
  };

  protected degreeTypeControlSignal = toSignal(this.formGroup.get('type')!.valueChanges, { initialValue: this.formGroup.get('type')!.value });

  protected degreeTypeFilteredOptions = computed(() => {
    const value = this.degreeTypeControlSignal() ?? '';
    return this.filterDegreeType(value);
  });

  filterDegreeType(value: DegreeTypeModel | string | null): DegreeTypeModel[] {
    if (value === null || value === undefined || value === '') {
      return this.degreeTypeOptions();
    }

    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    if (filterValue === '') {
      return this.degreeTypeOptions();
    }
    return this.degreeTypeOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }

  onSubmit(submitMod: ModalSubmitMode) {
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue() as DegreeModel
    });
  }
}
