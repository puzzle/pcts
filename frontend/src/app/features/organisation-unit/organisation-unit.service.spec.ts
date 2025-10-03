import { TestBed } from '@angular/core/testing';

import { OrganisationUnitService } from './organisation-unit.service';

describe('OrganisationUnitService', () => {
  let service: OrganisationUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrganisationUnitService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
