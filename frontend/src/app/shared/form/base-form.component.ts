import { Component, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { BaseFormActionsComponent } from '../base-form-actions/base-form-actions.component';

@Component({
  standalone: true,
  selector: 'app-form',
  imports: [ReactiveFormsModule,
    ScopedTranslationPipe,
    BaseFormActionsComponent],
  templateUrl: './base-form.component.html'
})
export class BaseFormComponent {
  formGroup = input.required<FormGroup>();

  submitted = output();
}
