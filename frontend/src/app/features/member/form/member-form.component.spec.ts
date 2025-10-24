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
import { TranslateModule } from '@ngx-translate/core';
import { provideRouter, Router } from '@angular/router';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MemberFormComponent } from './member-form.component';

class MockRouter {
  navigate = jest.fn();

  navigateByUrl = jest.fn();
}

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
      imports: [MemberFormComponent,
        TranslateModule.forRoot({})],
      providers: [
        provideRouter([]),
        provideNativeDateAdapter(),
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: OrganisationUnitService,
          useValue: organisationUnitServiceMock },
        { provide: Router,
          useClass: MockRouter }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberFormComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('member', null as any);
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should load organisationUnits', () => {
    fixture.componentRef.setInput('member', null as any);
    fixture.detectChanges();
    expect(component['organisationUnitsOptions']())
      .toStrictEqual(organisationUnits);
  });

  describe('addMember', () => {
    beforeEach(() => {
      fixture.componentRef.setInput('member', null as any);
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

      component['memberForm'].setValue(memberWithoutId);

      component.onSubmit();

      expect(router.navigate)
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
      expect(component['memberForm'].get('id')?.value)
        .toBe(1);
      expect(component['memberForm'].get('name')?.value)
        .toBe(member1.name);
      expect(component['memberForm'].get('lastName')?.value)
        .toBe(member1.lastName);
      expect(component['memberForm'].get('birthDate')?.value)
        .toBe(member1.birthDate);
      expect(component['memberForm'].get('abbreviation')?.value)
        .toBe(member1.abbreviation);
      expect(component['memberForm'].get('employmentState')?.value)
        .toBe(member1.employmentState);
      expect(component['memberForm'].get('dateOfHire')?.value)
        .toBe(member1.dateOfHire);
      expect(component['memberForm'].get('organisationUnit')?.value)
        .toBe(member1.organisationUnit);
    });

    it('should call updateMember', () => {
      const updateSpy = jest.spyOn(memberServiceMock, 'updateMember');

      component.onSubmit();

      expect(updateSpy)
        .toHaveBeenCalledWith(1, member1);

      updateSpy.mockRestore();
    });
  });
});
