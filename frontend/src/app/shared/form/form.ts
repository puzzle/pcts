import {Component, computed, effect, inject, input, Input, output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {routes} from '../../app.routes';
import {provideRouter, Router} from '@angular/router';
import {TranslatePipe} from '@ngx-translate/core';
import {MatButton} from '@angular/material/button';
import {CaseFormatter} from '../format/case-formatter';

@Component({
  selector: 'app-form',
  imports: [
    ReactiveFormsModule,
    TranslatePipe,
    MatButton
  ],
  templateUrl: './form.html',
  styleUrl: './form.scss'
})
export class Form {

  private readonly caseFormatter = inject(CaseFormatter);

  formGroup = input.required<FormGroup>();

  formName = input.required<string>();

  isEdit = input<boolean>(false);

  i18nPrefix = computed(() => `FORM.${this.caseFormatter.camelToSnake(this.formName())}`)

  submitted = output();

  canceled = output();
}
