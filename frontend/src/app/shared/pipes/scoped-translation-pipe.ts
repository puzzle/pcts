import { inject, Pipe, PipeTransform } from '@angular/core';
import { ScopedTranslationService } from '../services/scoped-translation.service';
import { InterpolationParameters } from '@ngx-translate/core';

@Pipe({
  name: 'scopedTranslation'
})
export class ScopedTranslationPipe implements PipeTransform {
  pctsTranslationService = inject(ScopedTranslationService);

  transform(value: string, params?: InterpolationParameters): string {
    return this.pctsTranslationService.instant(value, params);
  }
}
