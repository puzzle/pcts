import { Component, input } from '@angular/core';

export type HintBoxState = 'Hint' | 'Warning';

@Component({
  imports: [],
  selector: 'app-hint-box',
  styleUrl: './hint-box.component.scss',
  templateUrl: './hint-box.component.html'
})
export class HintBoxComponent {
  text = input.required<string>();

  state = input.required<HintBoxState>();
}
