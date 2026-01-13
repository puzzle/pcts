import { Component, computed, inject } from '@angular/core';
import { BaseModalComponent } from '../../../../shared/modal/base-modal.component';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { BaseFormComponent } from '../../../../shared/form/base-form.component';
import { InputFieldComponent } from '../../../../shared/input-field/input-field.component';
import { MatError, MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { provideI18nPrefix } from '../../../../shared/i18n-prefix.provider';
import { CertificateKind } from '../../../certificates/certificate-type/certificate-kind.enum';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { PctsFormErrorDirective } from '../../../../shared/pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../../../../shared/pcts-form-label/pcts-form-label.directive';
import { TranslateService } from '@ngx-translate/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { Location } from '@angular/common';
import { isValueInList } from '../../../../shared/form/form-validators';
import { BaseFormActionsComponent } from '../../../../shared/base-form-actions/base-form-actions.component';
import { MatButton, MatIconButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { MatMenu, MatMenuItem, MatMenuTrigger } from '@angular/material/menu';
import { MatIcon } from '@angular/material/icon';

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
    BaseFormActionsComponent,
    MatButton,
    ScopedTranslationPipe,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatIconButton,
    MatIcon
  ],
  templateUrl: './add-certificate.component.html',
  styleUrl: './add-certificate.component.scss',
  providers: [provideI18nPrefix('CERTIFICATE.FORM.ADD')]
})
export class AddCertificateComponent {
  private readonly fb = inject(FormBuilder);

  private readonly translateService = inject(TranslateService);

  private readonly certificateKindOptions: string[] = Object.values(CertificateKind);

  private readonly location = inject(Location);

  protected formGroup = this.fb.nonNullable.group({
    certificateKind: [null,
      [Validators.required,
        isValueInList(this.certificateKindOptions)]],
    startDate: ['',
      Validators.required],
    dateUntil: [''],
    comment: ['']
  });

  protected readonly console = console;

  onCancel() {
    this.location.back();
  }

  protected displayCertificateKind = (certificateKind: CertificateKind | string): string => {
    if (!certificateKind) {
      return '';
    }
    const translationKey = 'CERTIFICATE.KIND.VALUES.' + certificateKind;
    return this.translateService.instant(translationKey);
  };

  protected certificateKindControlSignal = toSignal(this.formGroup.get('certificateKind')!.valueChanges.pipe(map((value) => value ?? '')), {
    initialValue: this.formGroup.get('certificateKind')!.value ?? ''
  });

  protected certificateKindFilteredOptions = computed(() => {
    const value = this.certificateKindControlSignal() ?? '';
    return this.filterCertificateKind(value);
  });

  protected filterCertificateKind(value: string): string[] {
    const filterValue = value?.toLowerCase() || '';

    return this.certificateKindOptions.filter((option) => {
      const translationKey = 'CERTIFICATE.KIND.VALUES.' + option;
      const translatedValue = this.translateService.instant(translationKey);
      return translatedValue.toLowerCase()
        .includes(filterValue);
    });
  }

  onSubmit() {
    console.log('Submit');
  }
}
