import { Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { BaseModalComponent } from '../../../../shared/modal/base-modal.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BaseFormComponent } from '../../../../shared/form/base-form.component';
import { InputFieldComponent } from '../../../../shared/input-field/input-field.component';
import { MatError, MatFormField, MatInput, MatLabel, MatSuffix } from '@angular/material/input';
import { provideI18nPrefix } from '../../../../shared/i18n-prefix.provider';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { PctsFormErrorDirective } from '../../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../../shared/pcts-form-label/pcts-form-label.directive';
import { TranslateService } from '@ngx-translate/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Location } from '@angular/common';
import { isValueInListSignal } from '../../../../shared/form/form-validators';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';
import { MatIcon } from '@angular/material/icon';
import { ModalSubmitMode } from '../../../../shared/enum/modal-submit-mode.enum';
import { MemberService } from '../../member.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { CertificateTypeModel } from '../../../certificates/certificate-type/certificate-type.model';
import { CertificateTypeService } from '../../../certificates/certificate-type/certificate-type.service';
import { OrganisationUnitModel } from '../../../organisation-unit/organisation-unit.model';
import { MatDatepicker, MatDatepickerInput, MatDatepickerToggle } from '@angular/material/datepicker';

@Component({
  selector: 'app-add-certificate',
  imports: [
    BaseModalComponent,
    BaseFormComponent,
    FormsModule,
    InputFieldComponent,
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
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatIcon,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatSuffix
  ],
  templateUrl: './add-certificate.component.html',
  styleUrl: './add-certificate.component.scss',
  providers: [provideI18nPrefix('CERTIFICATE.FORM.ADD')]
})
export class AddCertificateComponent<D> implements OnInit {
  private readonly fb = inject(FormBuilder);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly route = inject(ActivatedRoute);

  private readonly memberService: MemberService = inject(MemberService);

  private readonly translateService = inject(TranslateService);

  private readonly certificateTypeOptions: WritableSignal<CertificateTypeModel[]> = signal([]);

  private readonly certificateTypeService = inject(CertificateTypeService);

  private readonly location = inject(Location);

  dialogRef = inject(MatDialogRef<AddCertificateComponent<D>>);

  dialogDat: D = inject(MAT_DIALOG_DATA);

  protected formGroup = this.fb.nonNullable.group({
    id: [null],
    member: [null],
    certificateType: [null,
      [Validators.required,
        isValueInListSignal(this.certificateTypeOptions)]],
    completedAt: ['',
      Validators.required],
    validUntil: [''],
    comment: ['']
  });

  protected readonly console = console;

  ngOnInit(): void {
    this.certificateTypeService.getAllCertificateTypes()
      .subscribe((organisationUnits) => {
        this.certificateTypeOptions.set(organisationUnits);
        this.formGroup.get('certificateType')
          ?.updateValueAndValidity();
      });
  }

  onCancel() {
    this.location.back();
  }

  protected displayCertificateTypes = (certificateType: CertificateTypeModel): string => {
    if (!certificateType) {
      return '';
    }
    const translationKey = 'CERTIFICATE.TYPE.VALUES.' + certificateType.name;
    return this.translateService.instant(translationKey);
  };

  protected certificateTypeControlSignal = toSignal(this.formGroup.get('certificateType')!.valueChanges, { initialValue: this.formGroup.get('certificateType')!.value });

  protected certificateTypeFilteredOptions = computed(() => {
    const value = this.certificateTypeControlSignal() ?? '';
    return this.filterCertificateKind(value);
  });

  private filterCertificateKind(value: OrganisationUnitModel | string | null): CertificateTypeModel[] {
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
    console.log(this.formGroup.getRawValue());
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue()
    });
  }
}
