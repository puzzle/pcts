import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericCvContentComponent } from './generic-cv-content.component';
import { degreeOverviewList } from '../../../../shared/test/test-data';
import { provideRouter } from '@angular/router';
import { GenericTableDataSource } from '../../../../shared/generic-table/generic-table-data-source';

describe('GenericCvContentComponent', () => {
  let component: GenericCvContentComponent<any>;
  let fixture: ComponentFixture<GenericCvContentComponent<any>>;
  let mockTableSource: GenericTableDataSource<any>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCvContentComponent],
      providers: [provideRouter([])]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericCvContentComponent);
    component = fixture.componentInstance;
    mockTableSource = new GenericTableDataSource([]);
  });

  it('should create', () => {
    fixture.componentRef.setInput('table', mockTableSource);
    fixture.componentRef.setInput('data', []);
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should sync input data to the table data source via effect', () => {
    fixture.componentRef.setInput('table', mockTableSource);
    fixture.componentRef.setInput('data', degreeOverviewList);

    fixture.detectChanges();

    expect(component.table())
      .toBe(mockTableSource as any);
    expect(mockTableSource.data)
      .toEqual(degreeOverviewList);
  });

  it('should handle updates when input data changes', () => {
    fixture.componentRef.setInput('table', mockTableSource);
    fixture.componentRef.setInput('data', []);
    fixture.detectChanges();

    expect(mockTableSource.data.length)
      .toBe(0);

    const newData = [degreeOverviewList[0]];
    fixture.componentRef.setInput('data', newData);
    fixture.detectChanges();

    expect(mockTableSource.data.length)
      .toBe(1);
  });
});
