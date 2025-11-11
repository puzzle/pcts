import { AfterViewChecked, Component, computed, effect, inject, input, signal, viewChild } from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import { MatSort, MatSortHeader } from '@angular/material/sort';
import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { CaseFormatter } from '../format/case-formatter';
import { GenCol, GenericTableDataSource } from './GenericTableDataSource';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-generic-table',
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatSort,
    MatSortHeader,
    MatTable,
    ScopedTranslationPipe,
    MatHeaderCellDef,
    MatNoDataRow,
    RouterLink
  ],
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.scss'
})
export class GenericTableComponent<T extends object> implements AfterViewChecked {
  caseFormatter = inject(CaseFormatter);

  idAttr = input<keyof T>();

  crudBasePath = input<string>('');

  dataSource = input.required<GenericTableDataSource<T>>();

  entries = signal<T[]>([]);

  columns = computed(() => this.dataSource().columnDefs);

  columnNames = computed(() => this.dataSource().columnDefs.map((e) => e.columnName));

  sort = viewChild(MatSort);

  constructor() {
    effect(() => {
      this.dataSource().sortingDataAccessor = (data: T, sortHeaderId: string) => {
        const find = this.columns()
          .find((e) => e.columnName == sortHeaderId);
        if (!find) {
          return (data[sortHeaderId as keyof T] + '').toLowerCase();
        }

        const date = new Date(find.getValue(data));
        const isDate = !isNaN(date?.getTime());
        if (isDate) {
          return date.getTime();
        }
        return this.getDisplayValue(find, data)
          .toLowerCase();
      };
      this.dataSource().sort = this.sort();
    });
  }


  ngAfterViewChecked(): void {
    this.entries.set(this.dataSource().data);
  }

  getFieldI18nKey(col: GenCol<T>) {
    return this.caseFormatter.camelToSnake(col.columnName);
  }

  protected getDisplayValue(col: GenCol<T>, entity: T): string {
    let value = col.getValue(entity) ?? '';
    col.pipes.forEach((formatter) => {
      value = formatter(value);
    });
    return value;
  }

  protected getRouterLink(entity: T) {
    const idAttr = this.idAttr();
    if (!idAttr) {
      return undefined;
    }
    return [this.crudBasePath(),
      entity[idAttr]].filter(Boolean)
      .join('/');
  }
}
