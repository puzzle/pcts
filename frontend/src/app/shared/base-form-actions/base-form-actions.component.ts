import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-form-actions',
  standalone: true,
  imports: [MatButton,
    ScopedTranslationPipe],
  templateUrl: './base-form-actions.component.html',
  styleUrl: 'base-form-actions.component.scss'
})
export class BaseFormActionsComponent {
  formGroup = input.required<FormGroup>();

  canceled = output();
}
