import { Component, effect, input } from '@angular/core';
import { GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { ColumnTemplateDirective } from '../../../../shared/generic-table/column-template/column-template.directive';
import { TypedTemplateDirective } from '../../../../shared/generic-table/type-template/typed-template.directive';
import { ExperienceTypePillComponent } from '../../../../shared/experience-type-pill/experience-type-pill.component';
import { ShowIfAdminDirective } from '../../../../core/auth/directive/show-if-admin.directive';

@Component({
  selector: 'app-generic-cv-content',
  standalone: true,
  imports: [
    ScopedTranslationPipe,
    GenericTableComponent,
    ColumnTemplateDirective,
    TypedTemplateDirective,
    ExperienceTypePillComponent,
    ExperienceTypePillComponent,
    ShowIfAdminDirective
  ],
  templateUrl: './generic-cv-content.component.html'
})
export class GenericCvContentComponent<T extends object> {
  idAttr = input<keyof T>();

  data = input.required<T[]>();

  table = input.required<GenericTableDataSource<T>>();

  crudBasePath = input<string>('');

  constructor() {
    effect(() => {
      const tableValue = this.table();
      const dataValue = this.data();

      if (dataValue) {
        tableValue.data = dataValue;
      }
    });
  }
}
