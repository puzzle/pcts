import { inject, Pipe, PipeTransform } from '@angular/core';
import { InterpolationParameters } from '@ngx-translate/core';
import { ScopedTranslationService } from '../i18n-prefix.provider';

type ScopedTranslationParams = InterpolationParameters & {
  prefix?: string;
};

@Pipe({
  name: 'scopedTranslation'
})
export class ScopedTranslationPipe implements PipeTransform {
  pctsTranslationService = inject(ScopedTranslationService);

  transform(value: string, params?: ScopedTranslationParams): string {
    if (params?.prefix) {
      return this.pctsTranslationService.instant(params.prefix + value, params);
    }

    return this.pctsTranslationService.instant(value, params);
  }
}
