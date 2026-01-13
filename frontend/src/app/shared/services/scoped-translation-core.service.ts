import { inject, Injectable } from '@angular/core';
import { InterpolationParameters, TranslateService } from '@ngx-translate/core';


interface Variation {
  prefix: string;
  key: string;
}

@Injectable({ providedIn: 'root' })
export class ScopedTranslationCoreService {
  private readonly I18N_KEY_SEPARATOR = '.';

  private readonly translateService = inject(TranslateService);

  public instant(key: string, params?: InterpolationParameters, i18nPrefix = ''): string {
    const keyList = this.generateKeyHierarchy(key, i18nPrefix);
    return this.getTranslation(keyList, params);
  }

  /**
   * Creates a prioritized, unique list of all possible translation keys.
   */
  private generateKeyHierarchy(key: string, i18nPrefix: string): string[] {
    const variations = this.getStringVariations(i18nPrefix, key);
    const allKeys = variations.flatMap((v) => this.getKeyList(v.prefix, v.key));
    console.log(allKeys);
    // De-duplicate the list to avoid redundant checks
    return [...new Set(allKeys)];
  }

  /**
   * Generates variations of prefix/key combinations.
   * Example: prefix 'a.b.c' and key 'd'
   * -> [ { p: 'a.b.c', k: 'd' }, { p: 'a.b', k: 'c.d' }, { p: 'a', k: 'b.c.d' } ]
   */
  private getStringVariations(inputPrefix: string, inputKey: string): Variation[] {
    const separator = this.I18N_KEY_SEPARATOR;
    const prefixParts = inputPrefix.split(separator);

    const variations: Variation[] = [{ prefix: inputPrefix,
      key: inputKey }];

    for (let i = prefixParts.length - 1; i > 0; i--) {
      const prefix = prefixParts.slice(0, i)
        .join(separator);
      const keySuffix = prefixParts.slice(i)
        .join(separator);

      variations.push({
        prefix,
        key: `${keySuffix}${separator}${inputKey}`
      });
    }

    return variations;
  }

  /**
   * Creates a list of keys by progressively shortening the prefix.
   * Example: prefix 'a.b.c' and suffix 'd'
   * -> [ 'a.b.c.d', 'a.b.d', 'a.d', 'd' ]
   */
  private getKeyList(prefix: string, suffix: string): string[] {
    if (!prefix) {
      return [suffix];
    }

    const separator = this.I18N_KEY_SEPARATOR;
    const keys: string[] = [];
    const prefixParts = prefix.split(separator);

    while (prefixParts.length > 0) {
      const currentPrefix = prefixParts.join(separator);
      keys.push(`${currentPrefix}${separator}${suffix}`);
      prefixParts.pop();
    }

    keys.push(suffix);
    return keys;
  }

  /**
   * Finds the first key in the list that has a valid translation.
   */
  private getTranslation(keyList: string[], params?: InterpolationParameters): string {
    for (const key of keyList) {
      const translation = this.translateService.instant(key, params);

      // instant() returns the key itself if no translation is found
      if (translation !== key) {
        return translation;
      }
    }

    // Fallback to the first (most specific) key if none are found
    return keyList[0] ?? '';
  }
}
