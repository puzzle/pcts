import { AfterViewInit, Directive, ElementRef, inject, Renderer2 } from '@angular/core';
import { MatFormField } from '@angular/material/form-field';
import { filter } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appPctsFormError]',
  standalone: true
})
export class PctsFormError implements AfterViewInit {
  public matFormField = inject(MatFormField);

  public renderer2 = inject(Renderer2);

  public elementRef = inject(ElementRef);

  public translateService = inject(TranslateService);

  ngAfterViewInit(): void {
    this.matFormFieldControl.control?.statusChanges
      .pipe(filter((status) => status === 'INVALID'))
      .subscribe(() => this.updateError());
  }

  private updateError(): void {
    const messages = this.getErrorMessages() ?? [];
    const translatedMessages = messages.map((msg) => this.translateService.instant(msg));
    const html = translatedMessages.join('<br>');
    this.renderer2.setProperty(this.elementRef.nativeElement, 'innerHTML', html);
  }

  getErrorMessages(): string[] {
    const control = this.matFormField?._control?.ngControl;
    console.log(control);
    if (!control || !control.touched || control.valid) {
      return [];
    }
    console.log('Hallo');

    return control.errors ? Object.keys(control.errors)
      .map((key) => `VALIDATION.${key.toUpperCase()}`)
      .slice(0, Number.MAX_SAFE_INTEGER) : [];
  }

  get matFormFieldControl(): NgControl {
    return this.matFormField._control.ngControl as NgControl;
  }
}
