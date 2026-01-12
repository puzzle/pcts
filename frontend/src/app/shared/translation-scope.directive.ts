import {
  Directive,
  input,
  inject,
  computed,
  Signal,
  TemplateRef,
  ViewContainerRef,
  Injector,
  OnInit
} from '@angular/core';
import { I18N_PREFIX } from './i18n-prefix.token';
import { ScopedTranslationService } from './i18n-prefix.provider';


@Directive({
  selector: '[appTranslationScope]',
  standalone: true
})
export class TranslationScopeDirective implements OnInit {
  readonly scopeInput = input.required<string>({ alias: 'appTranslationScope' });

  private readonly parentPrefix = inject(I18N_PREFIX, { optional: true,
    skipSelf: true });

  readonly fullPrefix: Signal<string> = computed(() => {
    const parent = this.parentPrefix ? this.parentPrefix : '';
    const current = this.scopeInput();

    return parent ? `${parent}.${current}` : current;
  });

  // 4. Structural Directive Dependencies
  private readonly vcr = inject(ViewContainerRef);

  private readonly tpl = inject(TemplateRef);

  private readonly injector = inject(Injector);

  ngOnInit(): void {
    console.log(this.fullPrefix());
    const customInjector = Injector.create({
      providers: [{
        provide: I18N_PREFIX,
        useValue: this.fullPrefix()
      },
      ScopedTranslationService],
      parent: this.injector // Link to the ElementInjector of this anchor
    });

    this.vcr.createEmbeddedView(this.tpl, null, { injector: customInjector });
  }
}
