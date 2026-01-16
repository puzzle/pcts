import { TestBed } from '@angular/core/testing';
import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { memberOverview1 } from '../../../shared/test/test-data';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';

describe('MemberDetailViewComponent (Jest)', () => {
  let memberServiceMock: jest.Mocked<MemberService>;
  let routerMock: jest.Mocked<Router>;
  let routeMock: ActivatedRoute;

  function setupTestBed(id: string | null) {
    memberServiceMock = {
      getMemberOverviewByMemberId: jest.fn()
    } as unknown as jest.Mocked<MemberService>;

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

    TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent,
        CrudButtonComponent],
      providers: [
        { provide: ActivatedRoute,
          useValue: routeMock },
        { provide: Router,
          useValue: routerMock },
        { provide: MemberService,
          useValue: memberServiceMock },
        provideTranslateService(),
        DatePipe
      ]
    });

    const fixture = TestBed.createComponent(MemberDetailViewComponent);
    return {
      fixture,
      component: fixture.componentInstance
    };
  }

  it('loads the member overview when id exists', () => {
    const { fixture, component } = setupTestBed('1');

    memberServiceMock.getMemberOverviewByMemberId.mockReturnValue(of(memberOverview1));

    fixture.detectChanges();

    expect(memberServiceMock.getMemberOverviewByMemberId)
      .toHaveBeenCalledWith(1);
    expect(component.member())
      .toEqual(memberOverview1.member);
    expect(component.degreeData())
      .toEqual(memberOverview1.cv.degrees);
    expect(component.experienceData())
      .toEqual(memberOverview1.cv.experiences);
    expect(component.certificateData())
      .toEqual(memberOverview1.cv.certificates);
    expect(component.leadershipExperienceData())
      .toEqual(memberOverview1.cv.leadershipExperiences);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('navigates back when id does not exist', () => {
    const { fixture, component } = setupTestBed(null);

    fixture.detectChanges();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['/member']);
    expect(memberServiceMock.getMemberOverviewByMemberId).not.toHaveBeenCalled();
    expect(component.member())
      .toBeNull();
  });
});
