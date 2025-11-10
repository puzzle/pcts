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
import { GenericTableDataSource, ProcessedTableColumn } from './GenericTableDataSource';


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
    MatNoDataRow
  ],
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.scss'
})
export class GenericTableComponent<T extends object> implements AfterViewChecked {
  caseFormatter = inject(CaseFormatter);

  idAttr = input.required<keyof T>();

  dataSource = input.required<GenericTableDataSource<T>>();

  entries = signal<T[]>([]);

  columns = computed(() => this.dataSource().processedColumns);

  columnNames = computed(() => this.dataSource().processedColumns.map((e) => e.columnName));

  sort = viewChild(MatSort);

  constructor() {
    effect(() => {
      this.dataSource().sort = this.sort();
    });
  }

  ngAfterViewChecked(): void {
    this.entries.set(this.dataSource().data);
  }

  getFieldI18nKey(col: ProcessedTableColumn<T>) {
    return this.caseFormatter.camelToSnake(col.columnName);
  }

  protected getDisplayValue(col: ProcessedTableColumn<T>, entity: T) {
    let value = col.getValue(entity) ?? '';
    col.pipes.forEach((formatter) => {
      value = formatter(value);
    });
    return value;
  }
}
