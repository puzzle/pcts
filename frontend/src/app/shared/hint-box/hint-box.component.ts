import { Component, input } from '@angular/core';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { HintBoxState } from './HintBoxState.enum';
import { MatIcon } from '@angular/material/icon';

abstract class HintBoxConfig {
  abstract icon: string;

  abstract classes: string[];
}
class InfoConfig extends HintBoxConfig {
  icon = 'info';

  classes = ['hint'];
}
class WarningConfig extends HintBoxConfig {
  icon = 'warning';

  classes = ['warning'];
}
const HINT_BOX_CONFIG = {
  [HintBoxState.HINT]: new InfoConfig(),
  [HintBoxState.WARNING]: new WarningConfig()
};

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
