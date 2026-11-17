
abstract class HintBoxConfig {
  abstract icon: string;

  abstract classes: string[];
}

export class InfoConfig extends HintBoxConfig {
  icon = 'info';

  classes = ['hint'];
}

export class WarningConfig extends HintBoxConfig {
  icon = 'warning';

  classes = ['warning'];
}
