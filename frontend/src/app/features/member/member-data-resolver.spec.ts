import { TestBed } from '@angular/core/testing';
import { memberDataResolver } from './member-data-resolver';
import { MemberService } from './member.service';
import { ActivatedRoute, ActivatedRouteSnapshot, convertToParamMap, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { MemberModel } from './member.model';
import { member1 } from '../../shared/test/test-data';

const mockRoute = (id: string | null): ActivatedRouteSnapshot => ({ paramMap: convertToParamMap(id ? { id } : {}) } as ActivatedRouteSnapshot);

describe('memberDataResolver', () => {
  let mockMemberService: jest.Mocked<MemberService>;
  let state: RouterStateSnapshot;

  beforeEach(() => {
    mockMemberService = {
      getMemberById: jest.fn()
    } as unknown as jest.Mocked<MemberService>;

    TestBed.configureTestingModule({
      providers: [{ provide: MemberService,
        useValue: mockMemberService },
      { provide: ActivatedRoute,
        useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }]
    });

    state = {} as RouterStateSnapshot;
  });

  it('should return an empty member object when id is null', (done) => {
    const route = mockRoute(null);

    const result$ = TestBed.runInInjectionContext(() => memberDataResolver(route, state)) as Observable<MemberModel>;

    result$.subscribe((member) => {
      expect(member)
        .toEqual({});
      expect(mockMemberService.getMemberById).not.toHaveBeenCalled();

      done();
    });
  });

  it('should call getMemberById and return a member when id is present', (done) => {
    const testId = '1';
    const route = mockRoute(testId);
    mockMemberService.getMemberById.mockReturnValue(of(member1));

    const result$ = TestBed.runInInjectionContext(() => memberDataResolver(route, state)) as Observable<MemberModel>;

    result$.subscribe((member) => {
      expect(mockMemberService.getMemberById)
        .toHaveBeenCalledWith(+testId);
      expect(mockMemberService.getMemberById)
        .toHaveBeenCalledTimes(1);

      expect(member)
        .toEqual(member1);

      done();
    });
  });
});
