import { inject, Pipe, PipeTransform } from '@angular/core';
import { ScopedTranslationService } from '../i18n-prefix.provider';

@Pipe({
  name: 'translatedModelSuffix'
})
export class TranslatedModelSuffixPipe implements PipeTransform {
  pctsTranslationService = inject(ScopedTranslationService);

  transform(prefix: string, suffix = ''): string {
    const modelKey = 'MODEL_NAME';
    const translatedModelName = this.pctsTranslationService.instant(modelKey);
    return [prefix,
      translatedModelName,
      suffix].join('-');
  }
}
