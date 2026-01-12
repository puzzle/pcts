import { Directive, inject, input, ResourceRef, TemplateRef } from '@angular/core';

type TemplateType<T> = T | T[] | undefined;

@Directive({

  selector: 'ng-template[appTypedTemplate]'
})
export class TypedTemplateDirective<T> {
  appTypedTemplate = input<ResourceRef<TemplateType<T>> | TemplateType<T>>();

  private readonly contentTemplate = inject(TemplateRef<T>);

  static ngTemplateContextGuard<T>(dir: TypedTemplateDirective<T>, ctx: unknown): ctx is { $implicit: T } {
    return true;
  }
}
