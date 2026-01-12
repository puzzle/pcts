import {
  Directive,
  input,
  inject,
  TemplateRef,
  ViewContainerRef,
  Injector,
  OnInit
} from '@angular/core';
import { provideI18nPrefix } from '../i18n-prefix.provider';

@Directive({
  selector: '[appTranslationScope]',
  standalone: true
})
export class TranslationScopeDirective implements OnInit {
  readonly scopeInput = input.required<string>({ alias: 'appTranslationScope' });

  // 4. Structural Directive Dependencies
  private readonly vcr = inject(ViewContainerRef);

  private readonly tpl = inject(TemplateRef);

  private readonly injector = inject(Injector);

  ngOnInit(): void {
    const customInjector = Injector.create({
      providers: [provideI18nPrefix(this.scopeInput())],
      parent: this.injector // Link to the ElementInjector of this anchor
    });

    this.vcr.createEmbeddedView(this.tpl, null, { injector: customInjector });
  }
}
