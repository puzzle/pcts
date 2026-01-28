import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MemberService } from './member.service';
import { MemberModel } from './member.model';
import {
  calculation1, calculation2,
  member1,
  member2,
  member4,
  memberDto1,
  memberDto2,
  memberOverview1,
  rolePointsList1
} from '../../shared/test/test-data';
import { provideHttpClient } from '@angular/common/http';
import { CalculationModel } from '../calculations/calculation.model';

describe('MemberService', () => {
  let service: MemberService;
  let httpMock: HttpTestingController;
  const API_URL = '/api/v1/members';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MemberService,
        provideHttpClient(),
        provideHttpClientTesting()]
    })
      .compileComponents();

    service = TestBed.inject(MemberService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('getAllMembers', () => {
    it('should call httpClient.get with the correct URL and return members', () => {
      const mockMembers: MemberModel[] = [member1,
        member2];

      service.getAllMembers()
        .subscribe((members) => {
          expect(members)
            .toEqual(mockMembers);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('GET');
      req.flush(mockMembers);
    });
  });

  describe('getMemberById', () => {
    it('should call httpClient.get with the correct URL for a given id', () => {
      const memberId = 1;

      service.getMemberById(memberId)
        .subscribe((member) => {
          expect(member)
            .toEqual(member1);
        });

      const req = httpMock.expectOne(`${API_URL}/${memberId}`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(member1);
    });
  });

  describe('addMember', () => {
    it('should call httpClient.post with the correct URL and member data', () => {
      service.addMember(member1)
        .subscribe((newMember) => {
          expect(newMember)
            .toEqual(member1);
        });

      const req = httpMock.expectOne(API_URL);
      expect(req.request.method)
        .toBe('POST');
      expect(req.request.body)
        .toEqual(memberDto1);
      req.flush(member1);
    });
  });

  describe('updateMember', () => {
    it('should call httpClient.put with the correct URL and member data', () => {
      const memberId = 1;

      service.updateMember(memberId, member1)
        .subscribe((updatedMember) => {
          expect(updatedMember)
            .toEqual(member1);
        });

      const req = httpMock.expectOne(`${API_URL}/${memberId}`);
      expect(req.request.method)
        .toBe('PUT');
      expect(req.request.body)
        .toEqual(memberDto1);
      req.flush(member1);
    });

    it('should call httpClient.put with the correct URL and with member data that contains valid null attributes', () => {
      const memberId = 4;

      service.updateMember(memberId, member4)
        .subscribe((updatedMember) => {
          expect(updatedMember)
            .toEqual(member4);
        });

      const req = httpMock.expectOne(`${API_URL}/${memberId}`);
      expect(req.request.method)
        .toBe('PUT');
      expect(req.request.body)
        .toEqual(memberDto2);
      req.flush(member4);
    });
  });
  describe('should get overview', () => {
    it('should call httpClient.get with the correct URL for a given id to get overview', () => {
      const memberId = 1;

      service.getMemberOverviewByMemberId(memberId)
        .subscribe((member) => {
          expect(member)
            .toEqual(memberOverview1);
        });

      const req = httpMock.expectOne(`api/v1/member-overviews/${memberId}`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(memberOverview1);
    });
  });
  describe('should get calculations by member id and optional role id', () => {
    const mockCalcs = [calculation1,
      calculation2] as CalculationModel[];

    it('should call httpClient.get with the correct URL and no roleId', () => {
      const memberId = 5;

      service.getCalculationsByMemberIdAndOptionalRoleId(memberId)
        .subscribe((calcs) => {
          expect(calcs)
            .toEqual(mockCalcs);
        });

      const req = httpMock.expectOne(`${API_URL}/${memberId}/calculations`);
      expect(req.request.method)
        .toBe('GET');
      expect(req.request.params.keys().length)
        .toBe(0);
      req.flush(mockCalcs);
    });

    it('should call httpClient.get with the correct URL and include roleId in query params', () => {
      const memberId = 5;
      const roleId = 2;

      service.getCalculationsByMemberIdAndOptionalRoleId(memberId, roleId)
        .subscribe((calcs) => {
          expect(calcs)
            .toEqual(mockCalcs);
        });

      const req = httpMock.expectOne((r) => r.url === `${API_URL}/${memberId}/calculations` &&
        r.params.has('roleId') &&
        r.params.get('roleId') === `${roleId}`);

      expect(req.request.method)
        .toBe('GET');
      req.flush(mockCalcs);
    });
  });

  describe('should get roles and points', () => {
    it('should call httpClient.get with the correct URL for a given id to get role and there points', () => {
      const memberId = 1;

      service.getPointsForActiveCalculationsForRoleByMemberId(memberId)
        .subscribe((rolePoints) => {
          expect(rolePoints)
            .toEqual(rolePointsList1);
        });

      const req = httpMock.expectOne(`${API_URL}/${memberId}/role-points`);
      expect(req.request.method)
        .toBe('GET');
      req.flush(rolePointsList1);
    });
  });

  describe('toDto', () => {
    it('should correctly convert a MemberModel to a MemberDto', () => {
      const resultDto = service.toDto(member1);
      expect(resultDto)
        .toEqual(memberDto1);
    });
  });
});
