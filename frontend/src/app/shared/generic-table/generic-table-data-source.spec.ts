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

  it('should allow configuring sortedBy via withSortedBy', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withSortedBy('name');

    expect(ds.sortedBy)
      .toBe('name');
  });

  it('should return true for all elements when filter is empty or null', () => {
    const ds = new GenericTableDataSource(columns, data);

    ds.setFilter(null);

    expect(ds.filteredData.length)
      .toBe(3);
  });

  it('should apply limit filter and update hasMoreEntriesToDisplay signal', async() => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(2);

    ds.setFilter(null);

    expect(ds.filteredData.length)
      .toBe(2);
    expect(ds.filteredData[0].id)
      .toBe(1);
    expect(ds.filteredData[1].id)
      .toBe(2);

    await Promise.resolve();
    expect(ds.hasMoreEntriesToDisplay())
      .toBe(true);
  });

  it('should combine multiple custom filter predicates', () => {
    const ds = new GenericTableDataSource<TestModel, string>(columns, data)
      .addFilterPredicate((item, filter) => item.value >= 20)
      .addFilterPredicate((item, filter) => item.name.includes(filter));

    ds.setFilter('a');

    expect(ds.filteredData.length)
      .toBe(2);
    expect(ds.filteredData.map((d) => d.name))
      .toEqual(['Beta',
        'Gamma']);
  });

  it('should apply added custom predicate (replacing old filterPredicate logic)', () => {
    const ds = new GenericTableDataSource<TestModel, string>(columns, data);

    ds.addFilterPredicate((item, filter) => item.name.includes(filter));
    ds.setFilter('Alpha');

    expect(ds.filteredData.length)
      .toBe(1);
    expect(ds.filteredData[0].name)
      .toBe('Alpha');
  });

  it('should return all elements when filter object is set but no custom predicates are added', () => {
    const ds = new GenericTableDataSource<TestModel, string>(columns, data);

    ds.setFilter('Alpha');

    expect(ds.filteredData.length)
      .toBe(3);
  });

  it('should ignore limit when toggleIgnoreLimit is enabled', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(1);

    ds.setFilter(null);
    expect(ds.filteredData.length)
      .toBe(1);

    ds.toggleIgnoreLimit();
    expect(ds.filteredData.length)
      .toBe(3);
  });

  it('should restore limit when toggleIgnoreLimit is toggled twice', () => {
    const ds = new GenericTableDataSource<TestModel, string>(columns, data)
      .withLimit(1);

    ds.toggleIgnoreLimit();
    ds.toggleIgnoreLimit();

    ds.setFilter(null);

    expect(ds.filteredData.length)
      .toBe(1);
  });

  it('reloadData should retrigger filtering', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withLimit(1);

    ds.setFilter(null);
    expect(ds.filteredData.length)
      .toBe(1);

    ds.withLimit(2);
    ds.reloadData();

    expect(ds.filteredData.length)
      .toBe(2);
  });

  it('should allow link flag', () => {
    const ds = new GenericTableDataSource(columns, data)
      .withDetailViewLink();

    expect(ds.shouldLink)
      .toBe(true);
  });
});
