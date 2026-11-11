import { Component, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';

export type HintBoxState = 'Hint' | 'Warning';

@Component({
  selector: 'app-hint-box',
  standalone: true,
  imports: [ScopedTranslationPipe],
  templateUrl: './hint-box.component.html',
  styleUrl: './hint-box.component.scss'
})
export class HintBoxComponent {
  text = input.required<string>();

  state = input.required<HintBoxState>();
}
