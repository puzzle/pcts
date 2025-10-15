import { MatDatepickerModule } from '@angular/material/datepicker';

import {
  Component, computed, contentChild,
  effect, ElementRef,
  inject,
  viewChild
} from '@angular/core';
import { NgControl, ReactiveFormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { MAT_FORM_FIELD, MatError, MatFormField, MatFormFieldControl, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { CaseFormatter } from '../format/case-formatter';
import { MatAutocomplete } from '@angular/material/autocomplete';

@Component({
  selector: 'app-input-field',
  imports: [
    MatLabel,
    MatFormField,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatIconModule,
    MatError,
    TranslatePipe
  ],
  templateUrl: './input-field.html',
  styleUrl: './input-field.scss',
  standalone: true,
  providers: [{ provide: MAT_FORM_FIELD,
    useValue: InputField }],
  viewProviders: [{ provide: MAT_FORM_FIELD,
    useValue: InputField }]
})
export class InputField {
  private readonly caseFormatter = inject(CaseFormatter);

  matFormField = viewChild.required(MatFormField);

  matFormFieldControl = contentChild.required(MatFormFieldControl);

  auto = contentChild.required(MatAutocomplete);

  elementRef = inject(ElementRef);

  i18nPrefix = '';

  ngControl = computed(() => this.matFormField()._control.ngControl as NgControl);

  labelName = computed(() => this.caseFormatter.camelToSnake([this.i18nPrefix,
    this.ngControl()?.name ?? ''].join('.')));

  constructor() {
    effect(() => {
      console.log(this.matFormFieldControl());
      console.log(this.auto());
      const test =
      // if(test || true){
        this.matFormField()._control = this.matFormFieldControl();
      // }
    });
    effect(() => {
      // this.auto().
    });

    this.i18nPrefix = this.elementRef.nativeElement.closest('form')?.name;
  }

  getErrorMessages(): string[] {
    const control = this.ngControl();
    if (!control || !control.touched || control.valid) {
      return [];
    }

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, Number.MAX_SAFE_INTEGER) : [];
  }
}
