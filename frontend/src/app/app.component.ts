import { Component, CUSTOM_ELEMENTS_SCHEMA, DOCUMENT, effect, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import '@puzzleitc/puzzle-shell';
import { LangChangeEvent, TranslatePipe, TranslateService } from '@ngx-translate/core';
import { NgOptimizedImage } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs';
import { UserService } from './core/auth/user.service';

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

  private readonly userService = inject(UserService);

  private readonly translateService = inject(TranslateService);

  private readonly document = inject(DOCUMENT);

  protected userName: string | null;

  private readonly currentLang = toSignal(this.translateService.onLangChange.pipe(map((event: LangChangeEvent) => event.lang)), {
    initialValue: this.translateService.getCurrentLang() || this.translateService.getFallbackLang() || this.translateService.getBrowserLang() || 'en'
  });

  constructor() {
    this.userName = this.userService.getName();

    effect(() => {
      this.setHtmlLangAttribute(this.currentLang());
    });
  }

  visitRoot(): void {
    this.router.navigate(['']);
  }

  handleLogout() {
    this.userService.logout();
  }

  private setHtmlLangAttribute(lang: string): void {
    if (lang && this.document?.documentElement) {
      this.document.documentElement.setAttribute('lang', lang);
    }
  }
}

