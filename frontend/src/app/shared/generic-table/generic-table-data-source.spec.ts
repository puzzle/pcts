import { GenericTableDataSource, GenCol } from './generic-table-data-source';

interface TestModel {
  id: number;
  name: string;
  value: number;
}

describe('GenCol', () => {
  it('should create column from attribute', () => {
    const col = GenCol.fromAttr<TestModel>('name');

    const model: TestModel = { id: 1,
      name: 'Test',
      value: 10 };

    expect(col.columnName)
      .toBe('name');
    expect(col.getValue(model))
      .toBe('Test');
    expect(col.pipes)
      .toEqual([]);
  });

  it('should create column from calculated value', () => {
    const col = GenCol.fromCalculated<TestModel>('doubleValue', (m) => m.value * 2);

    const model: TestModel = { id: 1,
      name: 'Test',
      value: 10 };

    expect(col.columnName)
      .toBe('doubleValue');
    expect(col.getValue(model))
      .toBe(20);
  });

  it('should allow custom sorting accessor', () => {
    const accessor = (m: TestModel) => m.value;
    const col = GenCol.fromAttr<TestModel>('value')
      .withCustomSortingAccessor(accessor);

    expect(col.sortingAccessor)
      .toBe(accessor);
  });
});

describe('GenericTableDataSource', () => {
  let data: TestModel[];
  let columns: GenCol<TestModel>[];

  beforeEach(() => {
    data = [{ id: 1,
      name: 'Alpha',
      value: 10 },
    { id: 2,
      name: 'Beta',
      value: 20 },
    { id: 3,
      name: 'Gamma',
      value: 30 }];

    columns = [GenCol.fromAttr<TestModel>('id'),
      GenCol.fromAttr<TestModel>('name'),
      GenCol.fromAttr<TestModel>('value')];
  });

  it('should initialize with data and column definitions', () => {
    const ds = new GenericTableDataSource(columns, data);

    expect(ds.data)
      .toEqual(data);
    expect(ds.columnDefs)
      .toEqual(columns);
    expect(ds.shouldLink)
      .toBe(false);
  });

  it('should update column definitions via setter', () => {
    const ds = new GenericTableDataSource(columns, data);
    const newCols = [GenCol.fromAttr<TestModel>('name')];

    ds.columnDefs = newCols;

    expect(ds.columnDefs)
      .toEqual(newCols);
  });

  it('should apply limit filter', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(2);

    ds.filter = 'any';
    ds._updateChangeSubscription();

    expect(ds.filteredData.length)
      .toBe(2);
    expect(ds.filteredData[0].id)
      .toBe(1);
    expect(ds.filteredData[1].id)
      .toBe(2);
  });

  it('should apply custom filter predicate with index', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withCustomFilterPredicate((item, _filter, index) => index === 1);

    ds.filter = 'test';
    ds._updateChangeSubscription();

    expect(ds.filteredData.length)
      .toBe(1);
    expect(ds.filteredData[0].name)
      .toBe('Beta');
  });

  it('should fallback to default filterPredicate when filterPredicateWithIndex allows', () => {
    const ds = new GenericTableDataSource(columns, data);

    ds.filterPredicate = (item, filter) => item.name.includes(filter);
    ds.filter = 'Alpha';
    ds._updateChangeSubscription();

    expect(ds.filteredData.length)
      .toBe(1);
    expect(ds.filteredData[0].name)
      .toBe('Alpha');
  });

  it('should ignore predicate when toggleIgnorePredicate is enabled', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(1);

    ds.filter = 'test';
    ds._updateChangeSubscription();
    expect(ds.filteredData.length)
      .toBe(1);

    ds.toggleIgnorePredicate();
    expect(ds.filteredData.length)
      .toBe(3);
  });

  it('should restore predicate when toggleIgnorePredicate is toggled twice', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(1);

    ds.toggleIgnorePredicate();
    ds.toggleIgnorePredicate();

    ds.filter = 'test';
    ds._updateChangeSubscription();

    expect(ds.filteredData.length)
      .toBe(1);
  });

  it('reloadData should retrigger filtering', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(1);

    ds.filter = 'test';
    ds.reloadData();
    ds._updateChangeSubscription();

    expect(ds.filteredData.length)
      .toBe(1);
  });

  it('should allow link flag', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withDetailViewLink();

    expect(ds.shouldLink)
      .toBe(true);
  });
});
