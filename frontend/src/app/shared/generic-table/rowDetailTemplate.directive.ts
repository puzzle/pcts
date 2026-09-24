import { Directive, inject, TemplateRef } from '@angular/core';

@Directive({
  selector: '[appExpandableRow]',
  standalone: true
})
export class RowDetailTemplateDirective {
  template = inject(TemplateRef);
}
