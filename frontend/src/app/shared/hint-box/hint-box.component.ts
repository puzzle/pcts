import { Component, computed, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { HintBoxState } from './HintBoxState.enum';
import { MatIcon } from '@angular/material/icon';

const HINT_BOX_ICONS = {
  [HintBoxState.HINT]: 'info',
  [HintBoxState.WARNING]: 'warning'
} as const satisfies Record<HintBoxState, string>;

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

  variant = input.required<HintBoxState>();

  protected readonly icon = computed(() => HINT_BOX_ICONS[this.variant()]);

  protected readonly HintBoxState = HintBoxState;
}
