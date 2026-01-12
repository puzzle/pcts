import {Directive, inject, input, ResourceRef, TemplateRef} from '@angular/core';

type TemplateType<T> = T | T[] | undefined;

@Directive({
  // eslint-disable-next-line @angular-eslint/directive-selector
  selector: 'ng-template[typedTemplate]'
})
export class TypedTemplateDirective<T> {
  typedTemplate = input<ResourceRef<TemplateType<T>> | TemplateType<T>>();
  private contentTemplate = inject(TemplateRef<T>)

  static ngTemplateContextGuard<T>(dir: TypedTemplateDirective<T>, ctx: unknown): ctx is { $implicit: T } {
    return true;
  }
}
