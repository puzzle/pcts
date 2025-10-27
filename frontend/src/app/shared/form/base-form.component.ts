import { Component, computed, inject, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { MatButton } from '@angular/material/button';
import { CaseFormatter } from '../format/case-formatter';
import {
  getI18nAddButton,
  getI18nAddTitle, getI18nEditButton,
  getI18nEditTitle,
  getI18nTitle
} from '../utils/i18n.helper';

@Component({
  standalone: true,
  selector: 'app-form',
  imports: [ReactiveFormsModule,
    TranslatePipe,
    MatButton],
  templateUrl: './base-form.component.html'
})
export class BaseFormComponent {
  private readonly caseFormatter = inject(CaseFormatter);

  formGroup = input.required<FormGroup>();

  formName = input.required<string>();

  isEdit = input<boolean>(false);

  i18nPrefix = computed(() => `${this.caseFormatter.camelToSnake(this.formName())}.FORM`);

  submitted = output();

  canceled = output();

  titleKey = computed(() => getI18nTitle(this.i18nPrefix()));

  addOrEditTitle = computed(() => {
    this.isEdit() ? getI18nEditTitle() : getI18nAddTitle();
  });

  addOrEditButton = computed(() => {
    this.isEdit() ? getI18nEditButton() : getI18nAddButton();
  });
}
