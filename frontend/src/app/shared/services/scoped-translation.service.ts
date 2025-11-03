import { inject, Injectable } from '@angular/core';
import { I18N_PREFIX } from '../i18n-prefix.token';
import { InterpolationParameters, TranslateService } from '@ngx-translate/core';


interface Variation {
  prefix: string;
  key: string;
}

@Injectable({ providedIn: 'any' })
export class ScopedTranslationService {
  private readonly i18nPrefix: string = inject(I18N_PREFIX);

  private readonly I18N_KEY_SEPARATOR = '.';

  private readonly translateService = inject(TranslateService);

  public instant(key: string, params?: InterpolationParameters) {
    const variations = this.getStringVariations(this.i18nPrefix, key);
    const keyList = variations.map((v) => this.getKeyList(v.prefix, v.key))
      .flat();
    return this.getTranslation(keyList, params);
  }

  private getStringVariations(inputPrefix: string, inputKey: string): Variation[] {
    const splitParts = inputPrefix.split(this.I18N_KEY_SEPARATOR);
    const variations: Variation[] = [];
    variations.push({ prefix: inputPrefix,
      key: inputKey });

    for (let i = splitParts.length - 1; i > 0; i--) {
      const prefix = splitParts.slice(0, i)
        .join(this.I18N_KEY_SEPARATOR);
      const key = splitParts.slice(i)
        .join(this.I18N_KEY_SEPARATOR) + this.I18N_KEY_SEPARATOR + inputKey;
      variations.push({ prefix,
        key });
    }

    return variations;
  }

  private getKeyList(prefix: string, suffix: string): string[] {
    if (!prefix) {
      return [suffix];
    }

    const parts = prefix.split(this.I18N_KEY_SEPARATOR);
    const prefixedKeys = parts.map((_, index) => parts.slice(0, parts.length - index)
      .join(this.I18N_KEY_SEPARATOR) + this.I18N_KEY_SEPARATOR + suffix);
    return prefixedKeys.concat(suffix);
  }

  private getTranslation(keyList: string[], params?: InterpolationParameters): string {
    for (const key of keyList) {
      const translation = this.translateService.instant(key, params);

      if (translation !== key) {
        return translation;
      }
    }

    return keyList[0] ?? '';
  }
}
