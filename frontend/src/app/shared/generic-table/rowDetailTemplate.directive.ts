import { Directive, inject, input, TemplateRef } from '@angular/core';
import { GenericTableDataSource } from './generic-table-data-source';

@Directive({
  selector: '[appExpandableRow]',
  standalone: true
})
export class RowDetailTemplateDirective<T> {
  appExpandableRow = input.required<GenericTableDataSource<T>>();

  template = inject(TemplateRef);

  static ngTemplateContextGuard<TContext>(dir: RowDetailTemplateDirective<TContext>,
    ctx: unknown): ctx is { $implicit: TContext } {
    return true;
  }
}
