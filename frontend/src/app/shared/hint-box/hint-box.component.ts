import { Component, computed, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { MatIcon } from '@angular/material/icon';

export type HintBoxVariant = 'hint' | 'warning';

export type HintBoxIcon = 'info' | 'warning';

const HINT_BOX_ICONS = {
  hint: 'info',
  warning: 'warning'
} as const satisfies Record<HintBoxVariant, HintBoxIcon>;

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

  variant = input.required<HintBoxVariant>();

  protected readonly icon = computed(() => HINT_BOX_ICONS[this.variant()]);
}
