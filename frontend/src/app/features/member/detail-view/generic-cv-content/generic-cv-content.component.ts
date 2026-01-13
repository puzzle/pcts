import { Component, effect, inject, input, signal } from '@angular/core';
import { GenCol, GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { ScopedTranslationPipe } from '../../../../shared/pipes/scoped-translation-pipe';
import { ColumnTemplateDirective } from '../../../../shared/generic-table/column-template/column-template.directive';
import { TypedTemplateDirective } from '../../../../shared/generic-table/type-template/typed-template.directive';
import { I18N_PREFIX } from '../../../../shared/i18n-prefix.token';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';

@Component({
  selector: 'app-generic-cv-content',
  standalone: true,
  imports: [
    ScopedTranslationPipe,
    GenericTableComponent,
    ColumnTemplateDirective,
    TypedTemplateDirective,
    CrudButtonComponent
  ],
  templateUrl: './generic-cv-content.component.html',
  styleUrl: './generic-cv-content.component.scss'
})
export class GenericCvContentComponent<T extends object> {
  i18nPrefix = inject(I18N_PREFIX);

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

  getExperienceBadgeClass(type: string): string {
    console.log(this.i18nPrefix);
    switch (type) {
      case 'Internship':
        return 'badge-internship';
      case 'Hackathon':
        return 'badge-hackathon';
      default:
        return 'badge-default';
    }
  }
}
