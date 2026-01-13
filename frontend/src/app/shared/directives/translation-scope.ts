import {
  Directive,
  input,
  inject,
  TemplateRef,
  ViewContainerRef,
  Injector, effect
} from '@angular/core';
import { provideI18nPrefix } from '../i18n-prefix.provider';

@Directive({
  selector: '[appTranslationScope]',
  standalone: true
})
export class TranslationScopeDirective {
  readonly scopeInput = input.required<string>({ alias: 'appTranslationScope' });

  private readonly vcr = inject(ViewContainerRef);

  private readonly tpl = inject(TemplateRef);

  private readonly injector = inject(Injector);

  constructor() {
    effect(() => {
      this.vcr.clear();
      const customInjector = Injector.create({
        providers: [provideI18nPrefix(this.scopeInput())],
        parent: this.injector
      });
      this.vcr.createEmbeddedView(this.tpl, null, { injector: customInjector });
    });
  }
}
