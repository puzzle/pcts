import { TestBed } from '@angular/core/testing';
import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { MemberModel } from '../member.model';
import { OrganisationUnitModel } from '../../organisation-unit/organisation-unit.model';
import { DateTime } from 'luxon';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';

describe('MemberDetailViewComponent (Jest)', () => {
  const mockOrganisationUnit: OrganisationUnitModel = {
    id: 1,
    name: '/mem'
  };

  const mockMember: MemberModel = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    birthDate: DateTime.now(),
    abbreviation: 'JD',
    employmentState: EmploymentState.EX_MEMBER,
    organisationUnit: mockOrganisationUnit,
    dateOfHire: DateTime.now()
  };

  let memberServiceMock: jest.Mocked<MemberService>;
  let routerMock: jest.Mocked<Router>;
  let routeMock: ActivatedRoute;

  function setupTestBed(id: string | null) {
    memberServiceMock = {
      getMemberById: jest.fn()
    } as unknown as jest.Mocked<MemberService>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    routeMock = {
      snapshot: {
        paramMap: {
          get: jest.fn()
            .mockReturnValue(id)
        }
      }
    } as unknown as ActivatedRoute;

    TestBed.configureTestingModule({
      imports: [MemberDetailViewComponent],
      providers: [
        { provide: ActivatedRoute,
          useValue: routeMock },
        { provide: Router,
          useValue: routerMock },
        { provide: MemberService,
          useValue: memberServiceMock },
        provideTranslateService(),
        { provide: MemberService,
          useValue: memberServiceMock },
        DatePipe
      ]
    });

    const fixture = TestBed.createComponent(MemberDetailViewComponent);
    return {
      fixture,
      component: fixture.componentInstance
    };
  }

  it('loads the member when id exists', () => {
    const { fixture, component } = setupTestBed('1');
    memberServiceMock.getMemberById.mockReturnValue(of(mockMember));

    fixture.detectChanges(); // triggers ngOnInit

    expect(memberServiceMock.getMemberById)
      .toHaveBeenCalledWith(1);
    expect(component.member())
      .toEqual(mockMember);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('redirects when no id is provided', () => {
    const { fixture } = setupTestBed(null);
    fixture.detectChanges();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['/member']);
  });

  it('redirects when service throws an error', () => {
    const { fixture } = setupTestBed('1');
    memberServiceMock.getMemberById.mockReturnValue(throwError(() => new Error('fail')));

    fixture.detectChanges();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['/member']);
  });

  it('handleEditClick navigates to /member/:id/edit', () => {
    const { fixture, component } = setupTestBed('1');
    memberServiceMock.getMemberById.mockReturnValue(of(mockMember));

    fixture.detectChanges();
    component.handleEditClick();

    // NOTE: router receives "1", not 1
    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['/member',
        '1',
        'edit']);
  });
});
