import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericTableComponent } from './generic-table.component';
import { GenCol, GenericTableDataSource } from './generic-table-data-source';
import { DegreeOverviewModel } from '../../features/member/detail-view/cv/degree-overview.model';
import { degreeOverviewList } from '../test/test-data';
import {
  GenericCvContentComponent
} from '../../features/member/detail-view/generic-cv-content/generic-cv-content.component';
import { signal } from '@angular/core';


describe('GenericTableComponent', () => {
  let component: GenericTableComponent<DegreeOverviewModel>;
  let fixture: ComponentFixture<GenericTableComponent<DegreeOverviewModel>>;
  let dataSource: GenericTableDataSource<DegreeOverviewModel>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCvContentComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericTableComponent<DegreeOverviewModel>);
    component = fixture.componentInstance;

    const columns = [
      GenCol.fromAttr<DegreeOverviewModel>('name'),
      GenCol.fromAttr<DegreeOverviewModel>('degreeTypeName'),
      GenCol.fromAttr<DegreeOverviewModel>('startDate'),
      GenCol.fromAttr<DegreeOverviewModel>('id')
    ];

    dataSource = new GenericTableDataSource(columns, degreeOverviewList);

    fixture.componentRef.setInput('dataSource', dataSource);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should initialize columns and columnNames from the provided data source', () => {
    expect(component.columns().length)
      .toBe(4);
    expect(component.columnNames())
      .toContain('degreeTypeName');
    expect(component.dataSource().data.length)
      .toBe(3);
  });

  describe('Sorting Logic (Effect)', () => {
    it('should assign the sortingAccessor to the data source via effect', () => {
      expect(dataSource.sortingDataAccessor)
        .toBeTruthy();
      expect(dataSource.sort)
        .toBeTruthy();
    });

    it('should correctly sort Date objects using the accessor', () => {
      const accessor = dataSource.sortingDataAccessor;
      const row = degreeOverviewList[0];

      const dateResult = accessor(row, 'startDate');

      expect(typeof dateResult)
        .toBe('number');
      expect(dateResult)
        .toBe(new Date('2015-09-01')
          .getTime());
    });

    it('should correctly sort Strings (case-insensitive)', () => {
      const accessor = dataSource.sortingDataAccessor;
      const row = degreeOverviewList[0];

      const stringResult = accessor(row, 'name');
      expect(stringResult)
        .toBe('bachelor of science in computer science');
    });
  });

  describe('Display and Formatting', () => {
    it('should retrieve values correctly using getDisplayValue', () => {
      const col = dataSource.columnDefs.find((c) => c.columnName === 'name')!;
      const row = degreeOverviewList[1];

      const val = component['getDisplayValue'](col, row);

      expect(val)
        .toBe('Master of Artificial Intelligence');
    });
  });

  describe('Interactions (Expansion & Filtering)', () => {
    it('should toggle expansion and notify data source', () => {
      const spy = jest.spyOn(dataSource, 'toggleIgnorePredicate');

      expect(component.isExpanded)
        .toBe(false);

      component.toggleButton();

      expect(component.isExpanded)
        .toBe(true);
      expect(spy)
        .toHaveBeenCalled();
    });

    it('should detect if filter is applied', () => {
      expect(component.isFilterApplied())
        .toBe(false);

      component.isExpanded = true;
      expect(component.isFilterApplied())
        .toBe(true);
      component.isExpanded = false;

      dataSource.filteredData = [degreeOverviewList[0]];
      expect(component.isFilterApplied())
        .toBe(true);
    });
  });

  describe('Router Integration', () => {
    it('should generate valid router link using idAttr', () => {
      fixture.componentRef.setInput('idAttr', 'id');
      fixture.componentRef.setInput('crudBasePath', 'degrees');
      fixture.detectChanges();

      const row = degreeOverviewList[2];

      const link = (component as any).getRouterLink(row);

      expect(link)
        .toBe('degrees/3');
    });

    it('should return undefined if idAttr is not provided', () => {
      fixture.componentRef.setInput('idAttr', undefined);
      fixture.detectChanges();

      const row = degreeOverviewList[0];
      const link = (component as any).getRouterLink(row);

      expect(link)
        .toBeUndefined();
    });
  });

  describe('Expandable Rows', () => {
    it('should add expand colum to columnNames when isRowExpansionEnabled is true', () => {
      (component as any).isRowExpansionEnabled = signal(true);

      fixture.componentRef.setInput('dataSource', new GenericTableDataSource(dataSource.columnDefs, dataSource.data));

      expect(component.columnNames())
        .toContain('expand');
    });

    it('should not add expand colum to columnNames when isRowExpansionEnabled is false', () => {
      (component as any).isRowExpansionEnabled = signal(false);
      fixture.componentRef.setInput('dataSource', dataSource);

      fixture.componentRef.setInput('dataSource', new GenericTableDataSource(dataSource.columnDefs, dataSource.data));

      expect(component.columnNames()).not.toContain('expand');
    });

    describe('Expansion Toggle Logic', () => {
      beforeEach(() => {
        fixture.componentRef.setInput('idAttr', 'id');
        fixture.detectChanges();
      });
      it('should add element when not already in expandedElements', () => {
        const row = degreeOverviewList[0];

        component.expandedElementIds = [];

        component.toggleRowExpansion(row);

        expect(component.expandedElementIds)
          .toContain(row.id);
      });

      it('should remove element when already in expandedElements', () => {
        const row = degreeOverviewList[0];

        component.expandedElementIds = [row.id];

        component.toggleRowExpansion(row);

        expect(component.expandedElementIds).not.toContain(row.id);
      });
    });
  });
});
