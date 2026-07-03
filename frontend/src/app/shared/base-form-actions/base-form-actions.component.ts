import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { FormGroup } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';

@Component({
  selector: 'app-form-actions',
  standalone: true,
  imports: [MatButton,
    ScopedTranslationPipe],
  templateUrl: './base-form-actions.component.html'
})
export class BaseFormActionsComponent {
  formGroup = input.required<FormGroup>();

  canceled = output();

  isInvalid = toSignal(this.formGroup().statusChanges.pipe(map((f) => f === 'INVALID')));
}
