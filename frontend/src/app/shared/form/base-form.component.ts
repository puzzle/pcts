import { Component, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';

@Component({
  standalone: true,
  selector: 'app-form',
  imports: [ReactiveFormsModule,
    MatButton,
    ScopedTranslationPipe],
  templateUrl: './base-form.component.html'
})
export class BaseFormComponent {
  formGroup = input.required<FormGroup>();

  submitted = output();

  canceled = output();
}
