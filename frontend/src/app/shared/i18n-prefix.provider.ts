import { Optional, Provider, SkipSelf } from '@angular/core';
import { I18N_PREFIX } from './i18n-prefix.token';
import { ScopedTranslationService } from './services/scoped-translation.service';

export function provideI18nPrefix(suffix: string, separator = '.'): Provider[] {
  return [{
    provide: I18N_PREFIX,
    useFactory: (parentPrefix: string | null): string => {
      if (!suffix) {
        return parentPrefix ?? '';
      }
      return parentPrefix
        ? parentPrefix + separator + suffix
        : suffix;
    },
    deps: [[new SkipSelf(),
      new Optional(),
      I18N_PREFIX]]
  },
  ScopedTranslationService];
}
