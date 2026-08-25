import { TestBed } from '@angular/core/testing';
import { GenericTableDataSourceService } from './generic-table-data-source.service';

describe('GenericTableDataSourceService', () => {
  let service: GenericTableDataSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GenericTableDataSourceService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
