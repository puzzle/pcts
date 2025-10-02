import { Component, Input } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    TranslateModule
  ],
  styleUrls: ['./error.component.css'],
  standalone: true
})
export class ErrorComponent {
  @Input() form!: FormGroup;

  @Input() formControlPath!: string;

  get formControl(): FormControl | null {
    const control = this.form.get(this.formControlPath);
    return control instanceof FormControl ? control : null;
  }

  getErrorMessages(): string[] | null {
    const control = this.formControl;
    if (!control || !control.invalid || !control.dirty && !control.touched) {
      return null;
    }

    return control.errors ? Object.keys(control.errors)
      .map((key) => key.toUpperCase()) : [];
  }
}
