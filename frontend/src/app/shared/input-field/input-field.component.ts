import { MatDatepickerModule } from '@angular/material/datepicker';

import {
  Component, contentChild,
  effect,
  viewChild
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_FORM_FIELD, MatError, MatFormField, MatFormFieldControl, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { PctsFormError } from '../pcts-form-error/pcts-form-error';
import { PctsFormLabel } from '../pcts-form-label/pcts-form-label';

@Component({
  selector: 'app-input-field',
  imports: [
    MatLabel,
    MatFormField,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatIconModule,
    MatError,
    PctsFormError,
    PctsFormLabel
  ],
  templateUrl: './input-field.component.html',
  styleUrl: './input-field.component.scss',
  standalone: true,
  providers: [{ provide: MAT_FORM_FIELD,
    useValue: InputFieldComponent }],
  viewProviders: [{ provide: MAT_FORM_FIELD,
    useValue: InputFieldComponent }]
})
export class InputFieldComponent {
  matFormField = viewChild.required(MatFormField);

  matFormFieldControl = contentChild.required(MatFormFieldControl);

  constructor() {
    effect(() => {
      if (this.matFormFieldControl()) {
        this.matFormField()._control = this.matFormFieldControl();
      }
    });
  }
}
