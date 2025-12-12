import { Component, input, output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-form',
  imports: [ReactiveFormsModule],
  templateUrl: './base-form.component.html'
})
export class BaseFormComponent {
  formGroup = input.required<FormGroup>();

  submitted = output();
}
