import { MatDatepickerModule } from '@angular/material/datepicker';

import {
  Component, contentChild,
  effect,
  viewChild
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {
  MAT_FORM_FIELD,
  MatError,
  MatFormField,
  MatFormFieldControl,
  MatLabel
} from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { PctsFormErrorDirective } from '../pcts-form-error/pcts-form-error.directive';
import { PctsFormLabelDirective } from '../pcts-form-label/pcts-form-label.directive';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';

@Component({
  selector: 'app-input-field',
  imports: [
    MatLabel,
    MatFormField,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatIconModule,
    MatError,
    PctsFormErrorDirective,
    PctsFormLabelDirective
  ],
  templateUrl: './input-field.component.html',
  standalone: true,
  providers: [{ provide: MAT_FORM_FIELD,
    useValue: InputFieldComponent }],
  viewProviders: [{ provide: MAT_FORM_FIELD,
    useValue: InputFieldComponent }]
})
export class InputFieldComponent {
  matFormField = viewChild.required(MatFormField);

  matFormFieldControl = contentChild.required(MatFormFieldControl);

  matAutocompleteTrigger = contentChild(MatAutocompleteTrigger);

  constructor() {
    effect(() => {
      if (this.matFormFieldControl()) {
        this.matFormField()._control = this.matFormFieldControl();
      }
    });

    effect(() => {
      const matAutocompleteTrigger = this.matAutocompleteTrigger();
      if (matAutocompleteTrigger) {
        matAutocompleteTrigger['_formField'] = this.matFormField();
      }
    });
  }
}
