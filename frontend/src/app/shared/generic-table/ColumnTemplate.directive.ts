import { Directive, inject, input, TemplateRef } from '@angular/core';

@Directive({
  selector: '[columnTemplate]',
  standalone: true
})
export class ColumnTemplateDirective {
  // Captures the template itself
  template = inject(TemplateRef);

  // Captures the input value, e.g. columnTemplate="status"
  columnName = input.required<string>({ alias: 'columnTemplate' });
}
