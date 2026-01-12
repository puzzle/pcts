import {
  inject,
  Injectable,
  Optional,
  Provider,
  SkipSelf,
  Type
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


export function provideI18nScope(tokenSourceType: Type<any>, separator = '.'): Provider {
  return {
    provide: I18N_PREFIX,
    useFactory: (parentPrefix: string | null, instance: any) => {
      console.log('tasdf');
      // Access the computed signal from the injected directive instance
      const currentSuffix = instance.scope();
      console.log(currentSuffix);
      if (!currentSuffix) {
        return parentPrefix ?? '';
      }
      const var234 = parentPrefix
        ? parentPrefix + separator + currentSuffix
        : currentSuffix;
      console.log('parent prefix:', parentPrefix);
      console.log(instance.scope());
      console.log(var234);
      return var234;
    },
    // We inject the parent token (optional) AND the directive instance itself
    deps: [[new Optional(),
      new SkipSelf(),
      I18N_PREFIX],
    tokenSourceType]
  };
}

@Injectable()
export class ScopedTranslationService {
  private readonly core = inject(ScopedTranslationCoreService);

  public readonly prefix = inject(I18N_PREFIX);

  public instant(key: string, params?: InterpolationParameters): string {
    return this.core.instant(key, params, this.prefix);
  }
}
