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
import { isCertificateTypeName } from '../../../../shared/form/form-validators';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { ModalSubmitMode } from '../../../../shared/enum/modal-submit-mode.enum';
import { CertificateTypeModel } from '../../../certificates/certificate-type/certificate-type.model';
import { CertificateTypeService } from '../../../certificates/certificate-type/certificate-type.service';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';
import { MenuButtonComponent } from '../../../../shared/menu-button/menu-button.component';
import { DialogResult, StrictlyTypedMatDialog } from '../../../../shared/strictly-typed-mat-dialog';
import { CertificateModel } from '../../../certificates/certificate.model';
import { MemberModel } from '../../member.model';
import { NoIdCertificate } from '../../../certificates/certificate.service';


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
    MenuButtonComponent
  ],
  templateUrl: './add-certificate.component.html',
  styleUrl: './add-certificate.component.scss',
  providers: [provideI18nPrefix('CERTIFICATE.FORM.ADD')]
})
export class AddCertificateComponent extends StrictlyTypedMatDialog<NoIdCertificate | undefined, DialogResult<CertificateModel>> implements OnInit {
  private readonly fb = inject(FormBuilder);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly certificateTypeOptions: WritableSignal<CertificateTypeModel[]> = signal([]);

  private readonly certificateTypeService = inject(CertificateTypeService);

  protected formGroup = this.fb.nonNullable.group({
    id: this.fb.control<null | number>(null),
    member: [{} as MemberModel],
    certificateType: [{} as CertificateTypeModel,
      [Validators.required,
        isCertificateTypeName(this.certificateTypeOptions)]],
    completedAt: [{} as Date,
      Validators.required],
    validUntil: this.fb.control<Date | null>(null),
    comment: this.fb.control<string | null>('')
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
    if (value === null || value === undefined || value === '' || Object.keys(value).length == 0) {
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
      submittedModel: this.formGroup.getRawValue()
    });
  }
}
