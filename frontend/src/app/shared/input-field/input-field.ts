import {MatDatepickerModule, MatDatepickerToggle} from '@angular/material/datepicker';
import {InputTypeEnum} from './input-type.enum';
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from '@angular/material/autocomplete';

import {Component, Input, OnInit, signal, Signal} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';
import {MatError, MatFormField, MatLabel, MatSuffix} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon'; // <-- Import this module

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
    MatOption,
    MatIconModule,
    MatDatepickerToggle
  ],
  templateUrl: './input-field.html',
  styleUrl: './input-field.scss',
  standalone: true
})
export class InputField<T> implements OnInit {
  protected readonly InputTypeEnum = InputTypeEnum;

  @Input({ required: true }) labelKey!: string;

  @Input({required: true}) formGroup!: FormGroup;

  @Input({required: true}) formControlPath!: string;

  @Input() type: InputTypeEnum = InputTypeEnum.TEXT;

  @Input() maxErrors = Number.MAX_SAFE_INTEGER;

  @Input() dropdownOptions: Signal<T[]> = signal([]);

  @Input() displayWith: (option: T) => string = (option) => String(option);

  get formControl(): FormControl {
    return this.formGroup.get(this.formControlPath) as FormControl;
  }

  ngOnInit(): void {
    if (this.type === InputTypeEnum.DROPDOWN && this.dropdownOptions.length === 0) {
      this.formControl.disable();
    }
  }

  getErrorMessages(): string[] {
    const control = this.formControl;
    if (!control || !control.touched || control.valid) {
      return [];
    }

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, this.maxErrors) : [];
  }
}
