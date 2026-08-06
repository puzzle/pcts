import { inject, Injectable } from '@angular/core';
import { InterpolationParameters, TranslateService } from '@ngx-translate/core';
import { TranslationKeyPath } from './translation-key-path';


@Injectable({ providedIn: 'root' })
export class ScopedTranslationCoreService {
  private readonly translateService = inject(TranslateService);

  public instant(key: string, params?: InterpolationParameters, i18nPrefix = ''): string {
    const path = TranslationKeyPath.fromPrefix(i18nPrefix);
    const candidates = path.candidatesFor(key);
    const translation = this.firstTranslation(candidates, params);

    if (translation !== undefined) {
      return translation;
    }

    // this.reportMiss(candidates);
    return path.fullyQualified(key);
  }

  private firstTranslation(candidates: readonly string[],
    params?: InterpolationParameters): string | undefined {
    for (const candidate of candidates) {
      const translation = this.lookup(candidate, params);

      if (translation !== undefined) {
        return translation;
      }
    }

    return undefined;
  }

  /**
   * Anti-corruption boundary for two ngx-translate quirks. Absence is reported
   * as `undefined` so no caller has to know about either.
   *
   *   1. `instant` signals "not found" by echoing the key back. This breaks if
   *      a custom MissingTranslationHandler returns anything other than the key.
   *   2. `instant` returns the raw subtree when the key names a node, e.g.
   *      `FORM.DELETE` → `{ ACTION: 'löschen' }`. That is not a translation and
   *      must not win the lookup.
   *
   * Probing without params keeps interpolation off the miss path; only the
   * winner is re-read with them.
   */
  private lookup(candidate: string, params?: InterpolationParameters): string | undefined {
    const probe: unknown = this.translateService.instant(candidate);

    if (typeof probe !== 'string' || probe === candidate) {
      return undefined;
    }

    return params === undefined ? probe : this.translateService.instant(candidate, params);
  }
}
