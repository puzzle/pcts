import { HintBoxState } from './HintBoxState.enum';

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

export const HINT_BOX_CONFIG = {
  [HintBoxState.HINT]: new InfoConfig(),
  [HintBoxState.WARNING]: new WarningConfig()
};
