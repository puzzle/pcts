import { TestBed } from '@angular/core/testing';
import { memberDataResolver } from './member-data-resolver';
import { MemberService } from './member.service';
import { ActivatedRoute, ActivatedRouteSnapshot, convertToParamMap, Router, RouterStateSnapshot } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { MemberModel } from './member.model';
import { member1 } from '../../shared/test/test-data';

const mockRoute = (id: string | null): ActivatedRouteSnapshot => ({ paramMap: convertToParamMap(id ? { id } : {}) } as ActivatedRouteSnapshot);

describe('memberDataResolver', () => {
  let mockMemberService: jest.Mocked<MemberService>;
  let mockRouter: { navigate: jest.Mock };
  let state: RouterStateSnapshot;

  beforeEach(() => {
    mockMemberService = {
      getMemberById: jest.fn()
    } as unknown as jest.Mocked<MemberService>;

    mockRouter = {
      navigate: jest.fn()
    };

    TestBed.configureTestingModule({
      providers: [{ provide: MemberService,
        useValue: mockMemberService },
      { provide: Router,
        useValue: mockRouter },
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

  it('should call getMemberById and return a member when id is valid', (done) => {
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

  it('should navigate and return EMPTY when id is not a number', (done) => {
    const route = mockRoute('abc');

    const result$ = TestBed.runInInjectionContext(() => memberDataResolver(route, state)) as Observable<MemberModel>;

    result$.subscribe({
      next: () => fail('Expected EMPTY'),
      complete: () => {
        expect(mockRouter.navigate)
          .toHaveBeenCalledWith(['/member']);
        expect(mockMemberService.getMemberById).not.toHaveBeenCalled();
        done();
      }
    });
  });

  it('should navigate and return EMPTY when getMemberById throws an error', (done) => {
    const route = mockRoute('1');
    mockMemberService.getMemberById.mockReturnValue(throwError(() => new Error('test error')));

    const result$ = TestBed.runInInjectionContext(() => memberDataResolver(route, state)) as Observable<MemberModel>;

    result$.subscribe({
      next: () => fail('Expected EMPTY'),
      complete: () => {
        expect(mockRouter.navigate)
          .toHaveBeenCalledWith(['/member']);
        expect(mockMemberService.getMemberById)
          .toHaveBeenCalled();
        done();
      }
    });
  });
});
