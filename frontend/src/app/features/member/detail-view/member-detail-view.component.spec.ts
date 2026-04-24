import { TestBed } from '@angular/core/testing';
import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { certificate1, leadershipExperience1, memberOverview1, rolePointsList1 } from '../../../shared/test/test-data';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';
import { PctsModalService } from '../../../shared/modal/pcts-modal.service';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { CertificateService } from '../../certificates/certificate.service';
import { MemberCalculationTableComponent } from './calculation-table/member-calculation-table.component';
import { LeadershipExperienceService } from '../../leadership-experiences/leadership-experience.service';

describe('MemberDetailViewComponent (Jest)', () => {
  let memberServiceMock: Partial<jest.Mocked<MemberService>>;
  let certificateService: Partial<jest.Mocked<CertificateService>>;
  let leadershipExperienceService: Partial<jest.Mocked<LeadershipExperienceService>>;
  let modalService: Partial<jest.Mocked<PctsModalService>>;
  let routerMock: jest.Mocked<Router>;
  let routeMock: ActivatedRoute;

  function setupTestBed(id: string | null) {
    memberServiceMock = {
      getMemberOverviewByMemberId: jest.fn(),
      getPointsForActiveCalculationsForRoleByMemberId: jest.fn(),
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
    } as Partial<jest.Mocked<MemberService>>;

    routerMock = {
      navigate: jest.fn(),
      url: '/member/1'
    } as any;

    routeMock = {
      snapshot: {
        paramMap: {
          get: jest.fn()
            .mockReturnValue(id)
        }
      }
    } as unknown as ActivatedRoute;

    modalService = {
      openModal: jest.fn()
    };

    certificateService = {
      addCertificate: jest.fn()
        .mockReturnValue(of(certificate1))
    } as Partial<jest.Mocked<CertificateService>>;

    leadershipExperienceService = {
      addLeadershipExperience: jest.fn()
        .mockReturnValue(of({ leadershipExperience1 }))
    } as Partial<jest.Mocked<LeadershipExperienceService>>;

    TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent,
        MemberCalculationTableComponent,
        CrudButtonComponent],
      providers: [
        { provide: ActivatedRoute,
          useValue: routeMock },
        { provide: Router,
          useValue: routerMock },
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: LeadershipExperienceService,
          useValue: leadershipExperienceService },
        { provide: PctsModalService,
          useValue: modalService },
        { provide: CertificateService,
          useValue: certificateService },
        provideTranslateService(),
        DatePipe
      ]
    });

    const fixture = TestBed.createComponent(MemberDetailViewComponent);
    memberServiceMock.getMemberOverviewByMemberId?.mockReturnValue(of(memberOverview1));
    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of([]));

    memberServiceMock.getPointsForActiveCalculationsForRoleByMemberId?.mockReturnValue(of(rolePointsList1));
    fixture.detectChanges();
    return {
      fixture,
      component: fixture.componentInstance
    };
  }

  it('loads the member overview and role points when an id exists', () => {
    const { component } = setupTestBed('1');

    // Service calls
    expect(memberServiceMock.getMemberOverviewByMemberId)
      .toHaveBeenCalledTimes(1);
    expect(memberServiceMock.getPointsForActiveCalculationsForRoleByMemberId)
      .toHaveBeenCalledTimes(1);
    expect(memberServiceMock.getPointsForActiveCalculationsForRoleByMemberId)
      .toHaveBeenCalledWith(1);

    // Member data
    expect(component.member())
      .toEqual(memberOverview1.member);

    // CV data
    expect(component.degreeData())
      .toEqual(memberOverview1.cv.degrees);
    expect(component.experienceData())
      .toEqual(memberOverview1.cv.experiences);
    expect(component.certificateData())
      .toEqual(memberOverview1.cv.certificates);
    expect(component.leadershipExperienceData())
      .toEqual(memberOverview1.cv.leadershipExperiences);
    expect(routerMock.navigate).not.toHaveBeenCalled();

    // Role points
    expect(component.rolePointList())
      .toEqual(rolePointsList1);
  });

  it('navigates back when id does not exist', () => {
    const { component } = setupTestBed(null);

    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['/member']);

    expect(memberServiceMock.getMemberOverviewByMemberId)
      .not.toHaveBeenCalled();
    expect(memberServiceMock.getPointsForActiveCalculationsForRoleByMemberId)
      .not.toHaveBeenCalled();

    expect(component.member())
      .toBeNull();
    expect(component.rolePointList())
      .toEqual([]);
  });

  describe('open certificate modal', () => {
    it('should save and close on SAVE mode', () => {
      const { component } = setupTestBed('1');

      modalService.openModal?.mockReturnValue({
        afterSubmitted: of({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: certificate1
        })
      } as any);

      const spyData = jest.spyOn(component, 'getData');

      component.openCertificateDialog();

      // Assert modal opened once, save called once, data refreshed once
      expect(modalService.openModal)
        .toHaveBeenCalledTimes(1);
      expect(certificateService.addCertificate)
        .toHaveBeenCalledTimes(1);
      expect(certificateService.addCertificate)
        .toHaveBeenCalledWith(certificate1);
      expect(spyData)
        .toHaveBeenCalledTimes(1);
    });

    it('should re-open dialog with empty data on ENTER_ANOTHER mode', () => {
      const { component } = setupTestBed('1');

      modalService.openModal?.mockReturnValueOnce({
        afterSubmitted: of({
          modalSubmitMode: ModalSubmitMode.ENTER_ANOTHER,
          submittedModel: certificate1
        })
      } as any);

      modalService.openModal?.mockReturnValue({
        afterSubmitted: of({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: certificate1
        })
      } as any);

      const spyData = jest.spyOn(component, 'getData');

      component.openCertificateDialog();

      expect(modalService.openModal)
        .toHaveBeenCalledTimes(2);
      expect(certificateService.addCertificate)
        .toHaveBeenCalledTimes(2);
      expect(certificateService.addCertificate)
        .toHaveBeenCalledWith(certificate1);
      expect(spyData)
        .toHaveBeenCalledTimes(2);
    });

    it('should re-open dialog with copied data on COPY mode', () => {
      const { component } = setupTestBed('1');

      modalService.openModal?.mockReturnValueOnce({
        afterSubmitted: of({
          modalSubmitMode: ModalSubmitMode.COPY,
          submittedModel: certificate1
        })
      } as any);

      modalService.openModal?.mockReturnValue({
        afterSubmitted: of({
          modalSubmitMode: ModalSubmitMode.SAVE,
          submittedModel: certificate1
        })
      } as any);

      const spyData = jest.spyOn(component, 'getData');

      component.openCertificateDialog();

      expect(modalService.openModal)
        .toHaveBeenCalledTimes(2);

      expect(modalService.openModal)
        .toHaveBeenNthCalledWith(2, expect.anything(), { data: certificate1 });

      expect(certificateService.addCertificate)
        .toHaveBeenCalledTimes(2);
      expect(spyData)
        .toHaveBeenCalledTimes(2);
    });
  });
});
