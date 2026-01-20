import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MemberService } from './member.service';
import { MemberModel } from './member.model';
import { member1, member2, member4, memberDto1, memberDto2, memberOverview1 } from '../../shared/test/test-data';
import { provideHttpClient } from '@angular/common/http';

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

  describe('toDto', () => {
    it('should correctly convert a MemberModel to a MemberDto', () => {
      const resultDto = service.toDto(member1);
      expect(resultDto)
        .toEqual(memberDto1);
    });
  });
});
