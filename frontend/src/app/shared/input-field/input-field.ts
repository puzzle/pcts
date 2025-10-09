import { MatDatepickerModule } from '@angular/material/datepicker';
import { InputTypeEnum } from './input-type.enum';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';

import { Component, Input, signal, Signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { MatError, MatFormField, MatLabel, MatSuffix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';

@Component({
  selector: 'app-input-field',
  imports: [
    TranslatePipe,
    MatLabel,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    MatError,
    MatDatepickerModule,
    MatSuffix,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption
  ],
  templateUrl: './input-field.html',
  styleUrl: './input-field.scss',
  standalone: true
})
export class InputField<T> {
  protected readonly InputTypeEnum = InputTypeEnum;

  @Input({ required: true }) labelKey!: string;

  @Input({ required: true }) formControl!: FormControl;

  @Input() type: InputTypeEnum = InputTypeEnum.TEXT;

  @Input() maxErrors = 1;

  @Input() dropdownOptions: Signal<T[]> = signal([]);

  @Input() displayWith: (option: T) => string = (option) => String(option);

  getErrorMessages(): string[] {
    const control = this.formControl;
    if (!control || !control.touched || !control.valid) {
      return [];
    }

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, this.maxErrors) : [];
  }
}
