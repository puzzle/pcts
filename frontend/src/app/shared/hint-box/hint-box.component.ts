import { Component, computed, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { MatIcon } from '@angular/material/icon';
export type HintBoxVariant = 'hint' | 'warning';

export interface HintBoxConfig { icon: string;
  class: string; }

const HINT_BOX_CONFIG = {
  hint: {
    icon: 'info',
    class: 'hint'
  },
  warning: {
    icon: 'warning',
    class: 'warning'
  }
} as const satisfies Record<HintBoxVariant, HintBoxConfig>;

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

  protected readonly config = computed(() => HINT_BOX_CONFIG[this.variant()]);
}
