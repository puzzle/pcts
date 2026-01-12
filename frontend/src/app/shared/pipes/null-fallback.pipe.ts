import { inject, Pipe, PipeTransform } from '@angular/core';
import { ScopedTranslationService } from '../i18n-prefix.provider';

@Pipe({
  name: 'nullFallback'
})
export class NullFallbackPipe implements PipeTransform {
  scopedTranslationService = inject(ScopedTranslationService);

  transform(value: any, fallback = 'ATTRIBUTE_NULL'): string {
    return value ?? this.scopedTranslationService.instant(fallback);
  }
}
