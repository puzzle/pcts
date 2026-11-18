import { Component, input, output } from '@angular/core';
import { MatButton } from '@angular/material/button';

@Component({
  imports: [MatButton],
  selector: 'app-cancel-button',
  templateUrl: './cancel-button.component.html'
})
export class CancelButtonComponent {
  text = input<string>();

  cancelClicked = output<void>();
}
