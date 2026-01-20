import { MatTableDataSource } from '@angular/material/table';

type Formatter = (value: any) => any;
type SortAccessor<T> = (model: T) => any;

export class GenCol<T> {
  columnName = '';

  getValue: (model: T) => any = () => {};

  pipes: Formatter[] = [];

  shouldLink = false;

  sortingAccessor: SortAccessor<T> = (model: T) => this.getValue(model);

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

  public withCustomSortingAccessor(sortValue: SortAccessor<T>) {
    this.sortingAccessor = sortValue;
    return this;
  }

  public withLink(shouldLink = true) {
    this.shouldLink = shouldLink;
    return this;
  }
}

export class GenericTableDataSource<T> extends MatTableDataSource<T> {
  private _columnDefs: GenCol<T>[] = [];

  private _ignorePredicate = false;

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

  public withLimit(limit: number) {
    this.filterPredicateWithIndex = (data: T, filter: string, index: number) => index < limit;
    return this;
  }

  public withCustomFilterPredicate(predicate: (data: T, filter: string, index: number) => boolean) {
    this.filterPredicateWithIndex = predicate;
    return this;
  }


  override _filterData(data: T[]) { // eslint-disable-line @typescript-eslint/naming-convention
    if (this._ignorePredicate) {
      this.filteredData = this.data;
    } else {
      this.filteredData = data.filter((obj: T, index: number) => this.filterPredicateWithIndex(obj, this.filter, index));
    }

    if (this.paginator) {
      this._updatePaginator(this.filteredData.length);
    }

    return this.filteredData;
  }

  filterPredicateWithIndex: (data: T, filter: string, index: number) => boolean = (data: T, filter: string, index: number) => {
    if (this.filter == null || this.filter === '') {
      return true;
    }
    return this.filterPredicate(data, filter);
  };

  toggleIgnorePredicate() {
    this._ignorePredicate = !this._ignorePredicate;
    this.reloadData();
  }

  reloadData() {
    this['_filter'].next('');
  }
}
