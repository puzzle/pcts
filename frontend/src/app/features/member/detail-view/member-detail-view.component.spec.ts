import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { memberOverview1, rolePointsList1 } from '../../../shared/test/test-data';
import { PctsModalService } from '../../../shared/modal/pcts-modal.service';
import { CertificateService } from '../../certificates/certificate.service';
import { LeadershipExperienceService } from '../../leadership-experiences/leadership-experience.service';

import { AuthService } from '../../../core/auth/auth.service';
import { DegreeService } from '../../degrees/degree.service';
import { ExperienceService } from '../../experiences/experience.service';


describe('MemberDetailViewComponent (Jest)', () => {
  let fixture: ComponentFixture<MemberDetailViewComponent>;
  let component: MemberDetailViewComponent;
  let memberServiceMock: Partial<jest.Mocked<MemberService>>;
  let certificateServiceMock: Partial<jest.Mocked<CertificateService>>;
  let leadershipExperienceServiceMock: Partial<jest.Mocked<LeadershipExperienceService>>;
  let experienceServiceMock: Partial<jest.Mocked<ExperienceService>>;
  let degreeServiceMock: Partial<jest.Mocked<DegreeService>>;
  let modalServiceMock: Partial<jest.Mocked<PctsModalService>>;
  let authServiceMock: jest.Mocked<AuthService>;
  let routerMock: jest.Mocked<Router>;
  let routeMock: ActivatedRoute;

  let mockDialogBuilder: any;

  beforeEach(() => {
    memberServiceMock = {
      getMemberOverviewByMemberId: jest.fn()
        .mockReturnValue(of(memberOverview1)),
      getPointsForActiveCalculationsForRoleByMemberId: jest.fn()
        .mockReturnValue(of(rolePointsList1)),
      getMemberById: jest.fn()
        .mockReturnValue(of(memberOverview1.member)),
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
        .mockReturnValue(of([]))
    } as Partial<jest.Mocked<MemberService>>;

    authServiceMock = {
      isAdmin: jest.fn()
        .mockReturnValue(true)
    } as unknown as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn(),
      url: '/member/1'
    } as any;

    routeMock = {} as unknown as ActivatedRoute;

    mockDialogBuilder = {
      withComponent: jest.fn()
        .mockReturnThis(),
      withOnSubmitMethod: jest.fn()
        .mockReturnThis(),
      withOnSuccessMethod: jest.fn()
        .mockReturnThis(),
      withSubmitOptionsForAdd: jest.fn()
        .mockReturnThis(),
      withSubmitOptionsForEdit: jest.fn()
        .mockReturnThis(),
      withI18nPrefix: jest.fn()
        .mockReturnThis(),
      build: jest.fn()
        .mockReturnValue(jest.fn())
    };

    modalServiceMock = {
      dialogOpener: jest.fn()
        .mockReturnValue(mockDialogBuilder)
    } as any;

    certificateServiceMock = {
      addCertificate: jest.fn(),
      updateCertificate: jest.fn(),
      getCertificateById: jest.fn()
    } as Partial<jest.Mocked<CertificateService>>;

    leadershipExperienceServiceMock = {
      addLeadershipExperience: jest.fn(),
      updateLeadershipExperience: jest.fn(),
      getLeadershipExperienceById: jest.fn()
    } as Partial<jest.Mocked<LeadershipExperienceService>>;

    experienceServiceMock = {
      addExperience: jest.fn(),
      updateExperience: jest.fn(),
      getExperienceById: jest.fn()
    } as Partial<jest.Mocked<ExperienceService>>;

    degreeServiceMock = {
      addDegree: jest.fn(),
      updateDegree: jest.fn(),
      getDegreeById: jest.fn()
    } as Partial<jest.Mocked<DegreeService>>;

    TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent],
      providers: [
        { provide: ActivatedRoute,
          useValue: routeMock },
        { provide: Router,
          useValue: routerMock },
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: LeadershipExperienceService,
          useValue: leadershipExperienceServiceMock },
        { provide: ExperienceService,
          useValue: experienceServiceMock },
        { provide: DegreeService,
          useValue: degreeServiceMock },
        { provide: PctsModalService,
          useValue: modalServiceMock },
        { provide: CertificateService,
          useValue: certificateServiceMock },
        { provide: AuthService,
          useValue: authServiceMock },
        provideTranslateService(),
        DatePipe
      ]
    });

    fixture = TestBed.createComponent(MemberDetailViewComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('memberId', 1);
    fixture.componentRef.setInput('tabIndex', 0);
    fixture.detectChanges();
  });

  it('loads the member overview and role points', () => {
    expect(memberServiceMock.getMemberOverviewByMemberId)
      .toHaveBeenCalledWith(1);
    expect(memberServiceMock.getPointsForActiveCalculationsForRoleByMemberId)
      .toHaveBeenCalledWith(1);
    expect(memberServiceMock.getMemberById)
      .toHaveBeenCalledWith(1);

    expect(component.degreeData())
      .toEqual(memberOverview1.cv.degrees);
    expect(component.experienceData())
      .toEqual(memberOverview1.cv.experiences);
    expect(component.certificateData())
      .toEqual(memberOverview1.cv.certificates);
    expect(component.leadershipExperienceData())
      .toEqual(memberOverview1.cv.leadershipExperiences);

    expect(component.rolePointsResource.value())
      .toEqual(rolePointsList1);
  });

  it('updates tab index via router navigation', () => {
    component.onTabIndexChange(2);

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([], {
        relativeTo: routeMock,
        queryParams: { tabIndex: 2 },
        queryParamsHandling: 'merge',
        replaceUrl: true
      });
  });

  describe('Modal Openers', () => {
    it('should call modal builders', () => {
      expect(modalServiceMock.dialogOpener)
        .toHaveBeenCalledTimes(8);

      expect(mockDialogBuilder.build)
        .toHaveBeenCalledTimes(8);
    });
  });
});
