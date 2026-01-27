import { Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { BaseModalComponent } from '../../../../shared/modal/base-modal.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BaseFormComponent } from '../../../../shared/form/base-form.component';
import { MatError, MatFormField, MatInput, MatLabel, MatSuffix } from '@angular/material/input';
import { provideI18nPrefix } from '../../../../shared/i18n-prefix.provider';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { PctsFormErrorDirective } from '../../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../../shared/pcts-form-label/pcts-form-label.directive';
import { toSignal } from '@angular/core/rxjs-interop';
import { isValueInListSignal } from '../../../../shared/form/form-validators';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { ModalSubmitMode } from '../../../../shared/enum/modal-submit-mode.enum';
import { CertificateTypeModel } from '../../../certificates/certificate-type/certificate-type.model';
import { CertificateTypeService } from '../../../certificates/certificate-type/certificate-type.service';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';
import { MenuButtonComponent } from '../../../../shared/menu-button/menu-button.component';
import { CertificateModel } from '../../../certificates/certificate.model';
import { MemberModel } from '../../member.model';
import { DialogResult, StrictlyTypedDialog } from '../../../../shared/modal/strictly-typed-dialog.helper';
import { InputFieldComponent } from '../../../../shared/input-field/input-field.component';


@Component({
  selector: 'app-add-certificate',
  imports: [
    BaseModalComponent,
    BaseFormComponent,
    FormsModule,
    MatInput,
    ReactiveFormsModule,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatError,
    MatFormField,
    MatLabel,
    MatOption,
    PctsFormErrorDirective,
    PctsFormLabelDirective,
    MatButton,
    ScopedTranslationPipe,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatSuffix,
    MenuButtonComponent,
    InputFieldComponent
  ],
  templateUrl: './add-certificate.component.html',
  styleUrl: './add-certificate.component.scss',
  providers: [provideI18nPrefix('CERTIFICATE.FORM.ADD')]
})
export class AddCertificateComponent extends StrictlyTypedDialog<CertificateModel | undefined, DialogResult<CertificateModel>> implements OnInit {
  private readonly fb = inject(FormBuilder);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly certificateTypeOptions: WritableSignal<CertificateTypeModel[]> = signal([]);

  private readonly certificateTypeService = inject(CertificateTypeService);

  formGroup = this.fb.nonNullable.group({
    id: [null as null | number],
    member: [null as MemberModel | null],
    certificateType: [null as CertificateTypeModel | null,
      [Validators.required,
        isValueInListSignal(this.certificateTypeOptions)]],
    completedAt: [null as Date | null,
      Validators.required],
    validUntil: [null as Date | null],
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
    this.certificateTypeService.getAllCertificateTypes()
      .subscribe((organisationUnits) => {
        this.certificateTypeOptions.set(organisationUnits);
        this.formGroup.get('certificateType')
          ?.updateValueAndValidity();
      });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected displayCertificateTypes = (certificateType: CertificateTypeModel): string => {
    return certificateType?.name ?? '';
  };

  protected certificateTypeControlSignal = toSignal(this.formGroup.get('certificateType')!.valueChanges, { initialValue: this.formGroup.get('certificateType')!.value });

  protected certificateTypeFilteredOptions = computed(() => {
    const value = this.certificateTypeControlSignal() ?? '';
    return this.filterCertificateType(value);
  });

  private filterCertificateType(value: CertificateTypeModel | string | null): CertificateTypeModel[] {
    if (value === null || value === undefined || value === '') {
      return this.certificateTypeOptions();
    }

    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    if (filterValue === '') {
      return this.certificateTypeOptions();
    }
    return this.certificateTypeOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }

  onSubmit(submitMod: ModalSubmitMode) {
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue() as CertificateModel
    });
  }
}
