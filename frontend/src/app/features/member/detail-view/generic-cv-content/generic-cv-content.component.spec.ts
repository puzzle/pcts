import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericCvContentComponent } from './generic-cv-content.component';
import { ScopedTranslationService } from '../../../../shared/i18n-prefix.provider';
import { I18N_PREFIX } from '../../../../shared/i18n-prefix.token';
import { DateAdapter, MAT_DATE_FORMATS, NativeDateAdapter } from '@angular/material/core';
import { GLOBAL_DATE_FORMATS } from '../../../../shared/format/date-format';
import { provideRouter } from '@angular/router';
import { NO_ERRORS_SCHEMA, Pipe, PipeTransform } from '@angular/core';
import { degreeOverviewList } from '../../../../shared/test/test-data';


@Pipe({
  name: 'scopedTranslation',
  standalone: true
})
class MockScopedTranslationPipe implements PipeTransform {
  transform(value: string): string {
    return value;
  }
}

class MockGenericTableDataSource<T> {
  data: T[] = [];
}

const translationMock = {
  instant: jest.fn()
    .mockImplementation((key: string) => key)
};

describe('GenericCvContentComponent', () => {
  let component: GenericCvContentComponent<any>;
  let fixture: ComponentFixture<GenericCvContentComponent<any>>;
  let mockTableSource: MockGenericTableDataSource<any>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCvContentComponent],
      providers: [
        provideRouter([]),
        { provide: ScopedTranslationService,
          useValue: translationMock },
        { provide: I18N_PREFIX,
          useValue: 'GLOBAL.DEFAULT.PREFIX' },
        { provide: DateAdapter,
          useClass: NativeDateAdapter },
        { provide: MAT_DATE_FORMATS,
          useValue: GLOBAL_DATE_FORMATS }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
      .overrideComponent(GenericCvContentComponent, {
        set: {
          imports: [MockScopedTranslationPipe]
        }
      })
      .compileComponents();

    fixture = TestBed.createComponent(GenericCvContentComponent);
    component = fixture.componentInstance;
    mockTableSource = new MockGenericTableDataSource();
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

    expect(component.tableDataSource())
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
