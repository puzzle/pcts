import { MatTableDataSource } from '@angular/material/table';


type Formatter = (value: any) => any;


export class GenCol<T> {
  columnName = '';

  getValue: (model: T) => any = () => {};

  pipes: Formatter[] = [];

  shouldLink = false;

  i18nPrefix?: string;

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

  public withLink(shouldLink = true) {
    this.shouldLink = shouldLink;
    return this;
  }

  public withI18nPrefix(value: string) {
    this.i18nPrefix = value;
    return this;
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

  override _filterData(data: T[]) {
    if (this.filter == null || this.filter === '') {
      this.filteredData = data;
    } else {
      this.filteredData = data.filter((obj: T, index: number) => this.filterPredicateWithIndex(obj, this.filter, index));
    }

    if (this.paginator) {
      this._updatePaginator(this.filteredData.length);
    }

    return this.filteredData;
  }

  filterPredicateWithIndex: (data: T, filter: string, index: number) => boolean = (data: T, filter: string, index: number) => {
    return this.filterPredicate(data, filter);
  };
}
