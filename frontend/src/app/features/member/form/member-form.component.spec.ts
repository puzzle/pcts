import { MemberFormComponent } from './member-form.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberService } from '../member.service';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';
import {
  member1, memberDto1,
  organisationUnit1,
  organisationUnit2,
  organisationUnit3,
  organisationUnit4
} from '../../../shared/test/test-data';
import { TranslateModule } from '@ngx-translate/core';
import { ActivatedRoute, provideRouter } from '@angular/router';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MemberModel } from '../member.model';

class MockActivatedRoute {
  public snapshot = {
    data: {} as any
  };

  public setData(memberData: MemberModel | null) {
    this.snapshot.data['memberData'] = memberData;
  }
}

describe('MemberFormComponent', () => {
  let component: MemberFormComponent;
  let fixture: ComponentFixture<MemberFormComponent>;
  let memberServiceMock: Partial<MemberService>;
  let organisationUnitServiceMock: Partial<OrganisationUnitService>;
  const mockActivatedRoute = new MockActivatedRoute();
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
        .mockReturnValue(of(member1)),
      toDto: jest.fn()
        .mockReturnValue(memberDto1)
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
        { provide: ActivatedRoute,
          useValue: mockActivatedRoute },
        { provide: MemberService,
          useValue: memberServiceMock },
        { provide: OrganisationUnitService,
          useValue: organisationUnitServiceMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberFormComponent);
    component = fixture.componentInstance;
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
    beforeEach(() => {
      mockActivatedRoute.setData(null);
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
        .toHaveBeenCalledWith(memberDto1);
    });
  });

  describe('updateMember', () => {
    beforeEach(() => {
      mockActivatedRoute.setData(member1);
      component.ngOnInit();
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

      component.loadMember();

      expect(updateSpy)
        .toHaveBeenCalledWith(1, memberDto1);

      updateSpy.mockRestore();
    });
  });
});

