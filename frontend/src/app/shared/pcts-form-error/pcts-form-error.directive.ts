import { AfterViewInit, Directive, ElementRef, inject, Renderer2 } from '@angular/core';
import { MatFormField } from '@angular/material/form-field';
import { filter } from 'rxjs';
import { NgControl } from '@angular/forms';
import { ScopedTranslationService } from '../i18n-prefix.provider';

@Directive({
  selector: '[appPctsFormError]',
  standalone: true,
  host: {
    'data-testid': 'validation-error'
  }
})
export class PctsFormErrorDirective implements AfterViewInit {
  public matFormField = inject(MatFormField);

  public renderer2 = inject(Renderer2);

  public elementRef = inject(ElementRef);

  public translateService = inject(ScopedTranslationService);

  ngAfterViewInit(): void {
    this.matFormFieldControl.control?.statusChanges
      .pipe(filter((status) => status === 'INVALID'))
      .subscribe(() => this.updateError());
  }

  updateError(): void {
    const messages = this.getErrorMessages() ?? [];
    const translatedMessages = messages.map((msg) => this.translateService.instant(msg));
    const html = translatedMessages.join('<br>');
    this.renderer2.setProperty(this.elementRef.nativeElement, 'innerHTML', html);
  }

  getErrorMessages(): string[] {
    const control = this.matFormField?._control?.ngControl;
    if (!control || control.valid) {
      return [];
    }

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, Number.MAX_SAFE_INTEGER) : [];
  }

  get matFormFieldControl(): NgControl {
    return this.matFormField._control.ngControl as NgControl;
  }
}
