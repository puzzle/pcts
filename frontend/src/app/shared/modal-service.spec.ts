import { TestBed } from '@angular/core/testing';

import { PctsModalService } from './pcts-modal.service';

describe('PctsModalService', () => {
  let service: PctsModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PctsModalService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
