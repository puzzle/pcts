import { Component, CUSTOM_ELEMENTS_SCHEMA, DOCUMENT, effect, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import '@puzzleitc/puzzle-shell';
import { LangChangeEvent, TranslatePipe, TranslateService } from '@ngx-translate/core';
import { NgOptimizedImage } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,
    TranslatePipe,
    NgOptimizedImage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppComponent {
  protected title = 'pcts';

  private readonly router = inject(Router);

  private readonly translateService = inject(TranslateService);

  private readonly document = inject(DOCUMENT);

  private readonly currentLang = toSignal(this.translateService.onLangChange.pipe(map((event: LangChangeEvent) => event.lang)), {
    initialValue: this.translateService.getCurrentLang() || this.translateService.getFallbackLang() || this.translateService.getBrowserLang() || 'en'
  });

  constructor() {
    effect(() => {
      this.setHtmlLangAttribute(this.currentLang());
    });
  }

  protected visitRoot(): void {
    this.router.navigate(['/member']);
  }

  private setHtmlLangAttribute(lang: string): void {
    if (lang && this.document?.documentElement) {
      this.document.documentElement.setAttribute('lang', lang);
    }
  }
}

