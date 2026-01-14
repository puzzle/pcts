import { Component, effect, input, signal } from '@angular/core';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
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

  columns = input.required<GenCol<T>[]>();

  tableDataSource = signal<GenericTableDataSource<T> | null>(null);

  constructor() {
    effect(() => {
      const cols = this.columns();
      const data = this.data();
      if (data) {
        if (this.tableDataSource()) {
          this.tableDataSource()!.columnDefs = cols;
          this.tableDataSource()!.data = data;
        } else {
          this.tableDataSource.set(new GenericTableDataSource<T>(cols, data));
        }
      }
    });
  }
}
