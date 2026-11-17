import { Component, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { HintBoxState } from './HintBoxState.enum';
import { HINT_BOX_CONFIG } from './hint-box-config';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-hint-box',
  standalone: true,
  imports: [ScopedTranslationPipe,
    MatIcon],
  templateUrl: './hint-box.component.html',
  styleUrl: './hint-box.component.scss'
})
export class HintBoxComponent {
  text = input.required<string>();

  state = input.required<HintBoxState>();

  protected readonly config = HINT_BOX_CONFIG;

  protected readonly HintBoxState = HintBoxState;
}
