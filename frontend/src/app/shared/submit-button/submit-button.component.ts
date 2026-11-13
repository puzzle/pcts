import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';

@Component({
  selector: 'app-submit-button',
  standalone: true,
  imports: [MatButton,
    ScopedTranslationPipe],
  styleUrl: './submit-button.component.scss',
  templateUrl: './submit-button.component.html'
})
export class SubmitButtonComponent {
  disabled = input<boolean>(false);

  isAdd = input.required<boolean>();

  saveClicked = output<void>();

  cancelClicked = output<void>();
}
