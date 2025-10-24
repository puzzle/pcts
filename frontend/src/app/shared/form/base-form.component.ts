import { Component, computed, inject, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { MatButton } from '@angular/material/button';
import { CaseFormatter } from '../format/case-formatter';

@Component({
  standalone: true,
  selector: 'app-form',
  imports: [ReactiveFormsModule,
    TranslatePipe,
    MatButton],
  templateUrl: './base-form.component.html',
  styleUrl: './base-form.component.scss'
})
export class BaseFormComponent {
  private readonly caseFormatter = inject(CaseFormatter);

  formGroup = input.required<FormGroup>();

  formName = input.required<string>();

  isEdit = input<boolean>(false);

  i18nPrefix = computed(() => `FORM.${this.caseFormatter.camelToSnake(this.formName())}`);

  submitted = output();

  canceled = output();
}
