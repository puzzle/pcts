import { inject, Pipe, PipeTransform } from '@angular/core';
import { InterpolationParameters } from '@ngx-translate/core';
import { ScopedTranslationService } from '../i18n-prefix.provider';

@Pipe({
  name: 'scopedTranslation'
})
export class ScopedTranslationPipe implements PipeTransform {
  pctsTranslationService = inject(ScopedTranslationService);

  transform(value: string, params?: InterpolationParameters): string {
    return this.pctsTranslationService.instant(value, params);
  }
}
