import { TestBed } from '@angular/core/testing';

import { MemberService } from './member.service';
import { provideHttpClient } from '@angular/common/http';

describe('MemberService', () => {
  let service: MemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    });
    service = TestBed.inject(MemberService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
