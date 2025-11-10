import { MatTableDataSource } from '@angular/material/table';


interface BaseColumnDef {
  formatters?: Formatter[];
}

type Formatter = (value: any) => any;

export interface FieldColumnDef<T> extends BaseColumnDef {
  type: 'field';
  field: keyof T;
}

export interface CalculatedColumnDef<T> extends BaseColumnDef {
  type: 'calculated';
  columnName: string;
  i18nPrefix?: string;
  getValue: (model: T) => any;
}

export type TableColumnDef<T> = FieldColumnDef<T> | CalculatedColumnDef<T>;

export interface ProcessedTableColumn<T> {
  columnName: string;
  getValue: (model: T) => any;
  pipes: Formatter[];
  i18nPrefix: string;
}


export class GenericTableDataSource<T> extends MatTableDataSource<T> {
  constructor(columnDefs: TableColumnDef<T>[], initialData?: T[]) {
    super(initialData);
    this.columnDefs = columnDefs;
  }

  private _processedColumns: ProcessedTableColumn<T>[] = [];

  public get processedColumns(): ProcessedTableColumn<T>[] {
    return this._processedColumns;
  }

  public set columnDefs(definitions: TableColumnDef<T>[]) {
    this._processedColumns = definitions.map((def) => {
      const baseProcessed = {
        pipes: def.formatters ?? [],
        i18nPrefix: ''
      };

      if (def.type === 'field') {
        return {
          ...baseProcessed,
          columnName: def.field.toString(),
          getValue: (model: T) => model[def.field]
        };
      }

      return {
        ...baseProcessed,
        i18nPrefix: def.i18nPrefix ?? '',
        columnName: def.columnName,
        getValue: def.getValue
      };
    });
  }
}
