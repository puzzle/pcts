import { TestBed } from '@angular/core/testing';
import { MemberDetailViewComponent } from './member-detail-view.component';
import { MemberService } from '../member.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { member1 } from '../../../shared/test/test-data';
import { CrudButtonComponent } from '../../../shared/crud-button/crud-button.component';


describe('MemberDetailViewComponent (Jest)', () => {
  let memberServiceMock: jest.Mocked<MemberService>;
  let routerMock: jest.Mocked<Router>;
  let routeMock: ActivatedRoute;

  function setupTestBed(id: string | null) {
    memberServiceMock = {
      getMemberById: jest.fn()
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
    memberServiceMock.getMemberById.mockReturnValue(of(member1));

    fixture.detectChanges();

    expect(memberServiceMock.getMemberById)
      .toHaveBeenCalledWith(1);
    expect(component.member())
      .toEqual(member1);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
