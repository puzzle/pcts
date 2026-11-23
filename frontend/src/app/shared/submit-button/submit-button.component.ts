import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-submit-button',
  standalone: true,
  imports: [MatButton],
  templateUrl: './submit-button.component.html'
})
export class SubmitButtonComponent {
  disabled = input<boolean>(false);

  saveClicked = output<void>();

  text = input<string>();
}
