import { Component } from '@angular/core';
import { BaseModalComponent } from '../../../../shared/modal/base-modal.component';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BaseFormComponent } from '../../../../shared/form/base-form.component';
import { InputFieldComponent } from '../../../../shared/input-field/input-field.component';
import { MatInput } from '@angular/material/input';
import { provideI18nPrefix } from '../../../../shared/i18n-prefix.provider';

@Component({
  selector: 'app-add-certificate',
  imports: [
    BaseModalComponent,
    BaseFormComponent,
    FormsModule,
    InputFieldComponent,
    MatInput,
    ReactiveFormsModule
  ],
  templateUrl: './add-certificate.component.html',
  styleUrl: './add-certificate.component.scss',
  providers: [provideI18nPrefix('CERTIFICATE.FORM.ADD')]
})
export class AddCertificateComponent {
  formGroup = new FormGroup({
    firstName: new FormControl(''),
    lastName: new FormControl('')
  });

  protected readonly console = console;
}
