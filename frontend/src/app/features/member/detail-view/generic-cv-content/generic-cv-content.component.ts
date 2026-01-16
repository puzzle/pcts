import { Component, effect, input, signal } from '@angular/core';
import { GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { ColumnTemplateDirective } from '../../../../shared/generic-table/column-template/column-template.directive';
import { TypedTemplateDirective } from '../../../../shared/generic-table/type-template/typed-template.directive';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';
import { ExperienceTypePillComponent } from '../../../../shared/experience-type-pill/experience-type-pill.component';

@Component({
  selector: 'app-generic-cv-content',
  standalone: true,
  imports: [
    ScopedTranslationPipe,
    GenericTableComponent,
    ColumnTemplateDirective,
    TypedTemplateDirective,
    CrudButtonComponent,
    ExperienceTypePillComponent
  ],
  templateUrl: './generic-cv-content.component.html',
  styleUrl: './generic-cv-content.component.scss'
})
export class GenericCvContentComponent<T extends object> {
  data = input.required<T[] | null>();

  table = input.required<GenericTableDataSource<T>>();

  tableDataSource = signal<GenericTableDataSource<T> | null>(null);

  constructor() {
    effect(() => {
      const tableValue = this.table();
      const dataValue = this.data();

      this.tableDataSource.set(tableValue);
      if (dataValue) {
        tableValue.data = dataValue;
      }
    });
  }
}
