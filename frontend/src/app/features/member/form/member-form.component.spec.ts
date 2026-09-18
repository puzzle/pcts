import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberService } from '../member.service';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';
import {
  member1,
  organisationUnit1,
  organisationUnit2,
  organisationUnit3,
  organisationUnit4, role1, role2
} from '../../../shared/test/test-data';
import { provideRouter, Router } from '@angular/router';
import { MemberFormComponent } from './member-form.component';
import { provideTranslateService } from '@ngx-translate/core';
import { MemberDetailViewComponent } from '../detail-view/member-detail-view.component';
import { RoleService } from '../../roles/role.service';

describe('MemberFormComponent', () => {
  let component: MemberFormComponent;
  let fixture: ComponentFixture<MemberFormComponent>;
  let memberServiceMock: Partial<MemberService>;
  let organisationUnitServiceMock: Partial<OrganisationUnitService>;
  let roleServiceMock: Partial<RoleService>;
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

    roleServiceMock = {
      getAllRoles: jest.fn()
        .mockReturnValue(of([role1,
          role2]))
    };

    TestBed.configureTestingModule({
      imports: [MemberFormComponent],
      providers: [
        provideRouter([{
          path: 'member/:id',
          component: MemberDetailViewComponent
        }]),
        provideTranslateService(),
        {
          provide: MemberService,
          useValue: memberServiceMock
        },
        {
          provide: OrganisationUnitService,
          useValue: organisationUnitServiceMock
        },
        {
          provide: RoleService,
          useValue: roleServiceMock
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberFormComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('member', null as any);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should load organisationUnits', () => {
    expect(component['organisationUnitsOptions']())
      .toStrictEqual(organisationUnits);
  });

  describe('addMember', () => {
    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should call addMember', () => {
      const addSpy = jest.spyOn(memberServiceMock, 'addMember');
      (component as any).roleOptions.set([role1,
        role2]);
      const memberWithoutId = {
        ...member1,
        id: 0,
        roles: [role1,
          role2]
      };

      component['memberForm'].setValue(memberWithoutId);

      component.onSubmit();

      expect(addSpy)
        .toHaveBeenCalledWith(memberWithoutId);
    });

    it('should navigate after adding a member', () => {
      const memberWithoutId = {
        ...member1,
        id: 0,
        roles: [role1,
          role2]
      };

      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, 'navigate');
      (component as any).roleOptions.set([role1,
        role2]);
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
      jest.spyOn(component['memberForm'], 'invalid', 'get')
        .mockReturnValue(false);

      component.onSubmit();

      expect(component['memberForm'].invalid)
        .toBeFalsy();

      expect(component['isEdit']())
        .toBeTruthy();

      expect(memberServiceMock.updateMember)
        .toHaveBeenCalledWith(1, { ...member1 });
    });
  });

  describe('Role Management', () => {
    let mockEvent: any;

    beforeEach(() => {
      mockEvent = {
        option: {
          value: null,
          deselect: jest.fn()
        }
      };

      component['memberForm'].get('roles')
        ?.setValue([role1]);
    });

    describe('removeRole', () => {
      it('should remove the specified role from the form control', () => {
        component['memberForm'].get('roles')
          ?.setValue([role1,
            role2]);

        component.removeRole(role1);

        expect(component['memberForm'].get('roles')?.value)
          .toEqual([role2]);
      });

      it('should not mutate the array if the role does not exist', () => {
        component.removeRole(role2);

        expect(component['memberForm'].get('roles')?.value)
          .toEqual([role1]);
      });
    });

    describe('selectRole', () => {
      it('should add a new role to the form control and deselect the option', () => {
        mockEvent.option.value = role2;

        component.selectRole(mockEvent);

        expect(component['memberForm'].get('roles')?.value)
          .toEqual([role1,
            role2]);
        expect(mockEvent.option.deselect)
          .toHaveBeenCalled();
      });

      it('should not add the role if it is already in the list, but still deselect', () => {
        mockEvent.option.value = role1;

        component.selectRole(mockEvent);

        expect(component['memberForm'].get('roles')?.value)
          .toEqual([role1]);
        expect(mockEvent.option.deselect)
          .toHaveBeenCalled();
      });

      it('should not add anything if the selected value is falsy, but still deselect', () => {
        mockEvent.option.value = null;

        component.selectRole(mockEvent);

        expect(component['memberForm'].get('roles')?.value)
          .toEqual([role1]);
        expect(mockEvent.option.deselect)
          .toHaveBeenCalled();
      });
    });
  });
});
