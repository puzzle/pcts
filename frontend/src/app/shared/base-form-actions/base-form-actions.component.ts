import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { FormGroup } from '@angular/forms';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { filter, map, startWith, switchMap } from 'rxjs';

@Component({
  selector: 'app-form-actions',
  standalone: true,
  imports: [MatButton,
    ScopedTranslationPipe],
  templateUrl: './base-form-actions.component.html'
})
export class BaseFormActionsComponent {
  formGroup = input.required<FormGroup | undefined>();

  canceled = output();

  private readonly formGroup$ = toObservable(this.formGroup);

  isInvalid = toSignal(this.formGroup$.pipe(filter((e): e is FormGroup => !!e), switchMap((form) => form.statusChanges.pipe(startWith(form.status))), map((status) => status === 'INVALID')), { initialValue: false });
}
