import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberService } from '../member.service';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';
import {
  member1,
  organisationUnit1,
  organisationUnit2,
  organisationUnit3,
  organisationUnit4
} from '../../../shared/test/test-data';
import { provideRouter, Router } from '@angular/router';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { MemberFormComponent } from './member-form.component';
import { provideTranslateService } from '@ngx-translate/core';
import { LuxonDateAdapter } from '@angular/material-luxon-adapter';
import { CUSTOM_LUXON_DATE_FORMATS } from '../../../shared/format/date-format';

describe('MemberFormComponent', () => {
  let component: MemberFormComponent;
  let fixture: ComponentFixture<MemberFormComponent>;
  let memberServiceMock: Partial<MemberService>;
  let organisationUnitServiceMock: Partial<OrganisationUnitService>;
  const organisationUnits = [
    organisationUnit1,
    organisationUnit2,
    organisationUnit3,
    organisationUnit4
  ];

  beforeEach(() => {
    memberServiceMock = {
      getMemberById: jest.fn()
        .mockReturnValue(of(member1)),
      addMember: jest.fn()
        .mockReturnValue(of(member1)),
      updateMember: jest.fn()
        .mockReturnValue(of(member1))
    };

    organisationUnitServiceMock = {
      getAllOrganisationUnits: jest.fn()
        .mockReturnValue(of(organisationUnits))
    };

    TestBed.configureTestingModule({
      imports: [MemberFormComponent],
      providers: [
        provideRouter([]),
        { provide: DateAdapter,
          useClass: LuxonDateAdapter },
        { provide: MAT_DATE_FORMATS,
          useValue: CUSTOM_LUXON_DATE_FORMATS },
        provideTranslateService(),
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: OrganisationUnitService,
          useValue: organisationUnitServiceMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberFormComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('member', null as any);
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should load organisationUnits', () => {
    fixture.detectChanges();
    expect(component['organisationUnitsOptions']())
      .toStrictEqual(organisationUnits);
  });

  describe('addMember', () => {
    beforeEach(() => {
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should call addMember', () => {
      const addSpy = jest.spyOn(memberServiceMock, 'addMember');
      const memberWithoutId = {
        ...member1,
        id: 0
      };

      component['memberForm'].setValue(memberWithoutId);

      component.onSubmit();

      expect(addSpy)
        .toHaveBeenCalledWith(memberWithoutId);
    });

    it('should navigate after adding a member', () => {
      const memberWithoutId = {
        ...member1,
        id: 0
      };
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, 'navigate');
      component['memberForm'].setValue(memberWithoutId);

      component.onSubmit();

      expect(navigateSpy)
        .toHaveBeenCalledWith(['/']);
    });
  });

  describe('updateMember', () => {
    beforeEach(() => {
      fixture.componentRef.setInput('member', member1);
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should isEdit be true', () => {
      expect(component['isEdit']())
        .toBe(true);
    });

    it('should load member data', () => {
      expect(component['memberForm'].getRawValue())
        .toEqual(member1);
    });

    it('should call updateMember', () => {
      component.onSubmit();

      expect(memberServiceMock.updateMember)
        .toHaveBeenCalledWith(1, member1);
    });
  });
});
