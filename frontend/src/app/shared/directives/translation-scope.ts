import { computed, Directive, effect, inject, input } from '@angular/core';
import { isArray } from '@ngx-translate/core';
import { provideI18nScope, ScopedTranslationService } from '../i18n-prefix.provider';


@Directive({
  selector: '[translationScope]',
  providers: [provideI18nScope(TranslationScope),
    ScopedTranslationService]
})
export class TranslationScope {
  scopedTranslationService = inject(ScopedTranslationService);
  // i18nPrefix = inject(I18N_PREFIX);

  translationScope = input.required<string | string[]>();

  scope = computed(() => {
    const scope = this.translationScope();
    return isArray(scope) ? scope.join('.') : scope;
  });

  constructor() {
    effect(() => {
      // console.log(this.i18nPrefix)
      console.log(this.scopedTranslationService.prefix);
      console.log(this.scope());
    });
  }
}
