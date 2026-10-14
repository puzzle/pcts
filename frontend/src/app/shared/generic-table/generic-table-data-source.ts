import { signal } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';

type Formatter = (value: any) => any;
type SortAccessor<T> = (model: T) => any;

export class GenCol<T> {
  columnName = '';

  getValue: (model: T) => any = () => {};

  pipes: Formatter[] = [];

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
}

export class GenericTableDataSource<T, F = any> extends MatTableDataSource<T> {
  private readonly _customPredicates: ((data: T, filter: F) => boolean)[] = [];

  private _limit?: number;

  private _columnDefs: GenCol<T>[] = [];

  private _ignoreLimit = false;

  private _filterObject: F | null = null;

  hasMoreEntriesToDisplay = signal(false);

  shouldLink = false;

  sortedBy: string;

  constructor(columnDefs: GenCol<T>[], initialData?: T[]) {
    super(initialData);
    this.columnDefs = columnDefs;
    this.sortedBy = this.columnDefs.map((e) => e.columnName)[0];
  }

  get columnDefs(): GenCol<T>[] {
    return this._columnDefs;
  }

  set columnDefs(value: GenCol<T>[]) {
    this._columnDefs = value;
  }

  public withLimit(limit: number) {
    this._limit = limit;
    return this;
  }

  public addFilterPredicate(predicate: (data: T, filter: F) => boolean) {
    this._customPredicates.push(predicate);
    return this;
  }

  public withDetailViewLink(shouldLink = true) {
    this.shouldLink = shouldLink;
    return this;
  }

  public withSortedBy(sortedBy: string) {
    this.sortedBy = sortedBy;
    return this;
  }

  public setFilter(filter: F) {
    this._filterObject = filter;
    this.reloadData();
  }

  override _filterData(data: T[]) { // eslint-disable-line @typescript-eslint/naming-convention
    let filteredEntries = data.filter((obj: T) => {
      if (this._filterObject == null) {
        return true;
      }

      return this.filterCustomPredicates(obj, this._filterObject);
    });

    const hasMoreEntriesToDisplay = this._limit !== undefined && filteredEntries.length > this._limit;

    /*
     * Defer signal update to the next microtask to avoid
     * conflicts with Angular's render process during template updates.
     */
    Promise.resolve()
      .then(() => {
        this.hasMoreEntriesToDisplay.set(hasMoreEntriesToDisplay);
      });

    if (hasMoreEntriesToDisplay && !this._ignoreLimit) {
      filteredEntries = filteredEntries.slice(0, this._limit);
    }

    if (this.paginator) {
      this._updatePaginator(filteredEntries.length);
    }

    this.filteredData = filteredEntries;
    return filteredEntries;
  }

  private filterCustomPredicates(data: T, filter: F): boolean {
    if (this._customPredicates && this._customPredicates.length > 0) {
      return this._customPredicates.every((predicate) => predicate(data, filter));
    }
    return true;
  }

  toggleIgnoreLimit() {
    this._ignoreLimit = !this._ignoreLimit;
    this.reloadData();
  }

  reloadData() {
    this.filter = this.filter; // eslint-disable-line no-self-assign
  }
}
