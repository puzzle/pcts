import {
  Component,
  computed,
  contentChildren,
  effect,
  input,
  TemplateRef,
  viewChild
} from '@angular/core';
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
import { camelToSnake } from '../utils/case-formatter';
import { GenCol, GenericTableDataSource } from './generic-table-data-source';
import { RouterLink } from '@angular/router';
import { NgTemplateOutlet } from '@angular/common';
import { ColumnTemplateDirective } from './column-template/column-template.directive';
import { TranslationScopeDirective } from '../translation-scope/translation-scope.directive';

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
    RouterLink,
    NgTemplateOutlet,
    TranslationScopeDirective
  ],
  templateUrl: './generic-table.component.html',
  styleUrl: './generic-table.component.scss'
})
export class GenericTableComponent<T extends object> {
  idAttr = input<keyof T>();

  crudBasePath = input<string>('');

  dataSource = input.required<GenericTableDataSource<T>>();

  isExpanded = false;

  isFilterApplied = () => {
    return this.dataSource().filteredData.length !== this.dataSource().data.length || this.isExpanded;
  };

  columns = computed(() => this.dataSource().columnDefs);

  columnNames = computed(() => this.dataSource().columnDefs.map((e) => e.columnName));

  sort = viewChild(MatSort);

  customTemplates = contentChildren(ColumnTemplateDirective);

  /*
   * 2. Create a Signal Map for O(1) lookup in the template
   * This prevents looping through the array for every single cell
   */
  templateMap = computed(() => {
    const map = new Map<string, TemplateRef<any>>();
    this.customTemplates()
      .forEach((dir) => {
        map.set(dir.columnName(), dir.template);
      });
    return map;
  });


  constructor() {
    effect(() => {
      this.dataSource().sortingDataAccessor = this.createSortingAccessor(this.columns());
      this.dataSource().sort = this.sort();
    });
  }

  toggleButton() {
    this.isExpanded = !this.isExpanded;
    this.dataSource()
      .toggleIgnorePredicate();
  }


  createSortingAccessor(columns: GenCol<T>[]) {
    return (data: T, sortHeaderId: string) => {
      const col = columns.find((c) => c.columnName === sortHeaderId)!;

      const raw = col.sortingAccessor(data);

      if (raw instanceof Date) {
        return raw.getTime();
      }

      if (typeof raw === 'number') {
        return raw;
      }

      return String(raw ?? '')
        .toLowerCase();
    };
  }

  getFieldI18nKey(col: GenCol<T>) {
    return camelToSnake(col.columnName);
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
