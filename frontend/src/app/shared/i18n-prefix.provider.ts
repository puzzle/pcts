import {
  inject,
  Injectable,
  Provider
} from '@angular/core';
import { InterpolationParameters } from '@ngx-translate/core';
import { I18N_PREFIX } from './i18n-prefix.token';
import { ScopedTranslationCoreService } from './services/scoped-translation-core.service';

export function provideI18nPrefix(suffix: string, separator = '.'): Provider[] {
  return [{
    provide: I18N_PREFIX,
    useFactory: () => {
      const parentPrefix = inject(I18N_PREFIX, { optional: true,
        skipSelf: true });

      if (!suffix) {
        return parentPrefix ?? '';
      }
      return parentPrefix
        ? parentPrefix + separator + suffix
        : suffix;
    }
  },
  ScopedTranslationService];
}

@Injectable()
export class ScopedTranslationService {
  private readonly core = inject(ScopedTranslationCoreService);

  public readonly prefix = inject(I18N_PREFIX);

  public instant(key: string, params?: InterpolationParameters): string {
    return this.core.instant(key, params, this.prefix);
  }
}
