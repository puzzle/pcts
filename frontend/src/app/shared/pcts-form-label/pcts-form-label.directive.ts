import { AfterViewInit, Directive, ElementRef, inject, Renderer2 } from '@angular/core';
import { camelToSnake } from '../utils/case-formatter';
import { NgControl } from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
import { ScopedTranslationService } from '../i18n-prefix.provider';

@Directive({
  selector: '[appPctsFormLabel]',
  standalone: true
})
export class PctsFormLabelDirective implements AfterViewInit {
  public matFormField = inject(MatFormField);

  public translateService = inject(ScopedTranslationService);

  public renderer2 = inject(Renderer2);

  public elementRef = inject(ElementRef);

  ngAfterViewInit(): void {
    const controlName = this.matFormFieldControl?.name?.toString() ?? '';
    const labelKey = camelToSnake(controlName);
    if (labelKey === '') {
      return;
    }
    const translatedLabel = this.translateService.instant(labelKey);
    this.renderer2.setProperty(this.elementRef.nativeElement, 'innerHTML', translatedLabel);
  }


  get matFormFieldControl(): NgControl {
    return this.matFormField._control.ngControl as NgControl;
  }
}
