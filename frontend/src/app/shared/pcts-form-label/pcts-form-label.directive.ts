import { AfterViewInit, Directive, ElementRef, inject, Renderer2 } from '@angular/core';
import { CaseFormatter } from '../format/case-formatter';
import { NgControl } from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
import { TranslateService } from '@ngx-translate/core';

@Directive({
  selector: '[appPctsFormLabel]',
  standalone: true
})
export class PctsFormLabelDirective implements AfterViewInit {
  private readonly caseFormatter = inject(CaseFormatter);

  public matFormField = inject(MatFormField);

  public translateService = inject(TranslateService);

  i18nPrefix = '';

  public renderer2 = inject(Renderer2);

  public elementRef = inject(ElementRef);

  ngAfterViewInit(): void {
    this.i18nPrefix = this.elementRef.nativeElement.closest('form')?.name;

    const labelKey = this.caseFormatter.camelToSnake([this.i18nPrefix,
      this.matFormFieldControl?.name ?? ''].join('.'));

    this.translateService.get(labelKey)
      .subscribe((translatedLabel) => {
        this.renderer2.setProperty(this.elementRef.nativeElement, 'innerHTML', translatedLabel);
      });
  }


  get matFormFieldControl(): NgControl {
    return this.matFormField._control.ngControl as NgControl;
  }
}
