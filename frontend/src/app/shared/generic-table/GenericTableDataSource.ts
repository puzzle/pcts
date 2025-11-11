import { MatTableDataSource } from '@angular/material/table';


type Formatter = (value: any) => any;


export class GenCol<T> {
  columnName = '';

  getValue: (model: T) => any = () => {};

  pipes: Formatter[] = [];

  protected constructor() {}

  public static fromAttr<T>(field: keyof T, pipes: Formatter[] = []): GenCol<T> {
    const genCol = new GenCol<T>();
    genCol.getValue = (model: T) => model[field];
    genCol.columnName = field.toString();
    genCol.pipes = pipes;
    return genCol;
  }

  public static fromCalculated<T>(columnName: string, getValue: (model: T) => any, pipes: Formatter[] = []): GenCol<T> {
    const genCol = new GenCol<T>();
    genCol.getValue = getValue;
    genCol.columnName = columnName;
    genCol.pipes = pipes;
    return genCol;
  }
}


export class GenericTableDataSource<T> extends MatTableDataSource<T> {
  private _columnDefs: GenCol<T>[] = [];

  constructor(columnDefs: GenCol<T>[], initialData?: T[]) {
    super(initialData);
    this.columnDefs = columnDefs;
  }


  get columnDefs(): GenCol<T>[] {
    return this._columnDefs;
  }

  set columnDefs(value: GenCol<T>[]) {
    this._columnDefs = value;
  }
}
