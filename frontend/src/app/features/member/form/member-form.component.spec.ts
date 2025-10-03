import { OrganisationUnitModel } from '../../organisation-unit/organisation-unit.model';
import { MemberModel } from '../member.model';
import { EmploymentState } from '../../../shared/enum/employment-state.enum';
import { of } from 'rxjs';
import { MemberService } from '../member.service';
import { OrganisationUnitService } from '../../organisation-unit/organisation-unit.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { MemberFormComponent } from './member-form.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

const mockOrganisationUnits: OrganisationUnitModel[] = [{ id: 1,
  name: 'org 1' },
{ id: 1,
  name: 'org 2' }];

const mockMember: MemberModel = {
  id: 1,
  name: 'name',
  lastName: 'lastname',
  birthday: new Date(2000, 1, 1),
  abbreviation: 'abbreviation',
  dateOfHire: new Date(2000, 1, 1),
  organisationUnit: mockOrganisationUnits[0],
  employmentState: EmploymentState.MEMBER
};

const memberServiceMock: Partial<MemberService> = {
  getMemberById: jest.fn()
    .mockReturnValue(of([mockMember])),
  addMember: jest.fn()
    .mockReturnValue(of([mockMember])),
  updateMember: jest.fn()
    .mockReturnValue(of([mockMember]))
};

const organisationUnitServiceMock: Partial<OrganisationUnitService> = {
  getAllOrganisationUnits: jest.fn()
    .mockReturnValue(of([mockOrganisationUnits]))
};

const routerMock: Partial<Router> = {
  navigate: jest.fn()
};

const translateServiceMock: Partial<TranslateService> = {
  get: jest.fn(),
  instant: jest.fn()
};

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: (key: string) => {
        return key === 'id' ? null : null;
      }
    }
  }
};

describe('MemberFormComponent', () => {
  let component: MemberFormComponent;
  let fixture: ComponentFixture<MemberFormComponent>;

  beforeEach(async() => {
    (memberServiceMock.getMemberById as jest.Mock).mockReturnValue(of([mockMember]));
    (memberServiceMock.addMember as jest.Mock).mockReturnValue(of([mockMember]));
    (memberServiceMock.updateMember as jest.Mock).mockReturnValue(of([mockMember]));
    (organisationUnitServiceMock.getAllOrganisationUnits as jest.Mock).mockReturnValue(of([mockOrganisationUnits]));

    (translateServiceMock.instant as jest.Mock).mockImplementation((key: string) => {
      if (key.includes(EmploymentState.MEMBER)) {
        return 'Member';
      }
      if (key.includes(EmploymentState.EX_MEMBER)) {
        return 'Ex Member';
      }
      if (key.includes(EmploymentState.APPLICANT)) {
        return 'Applicant';
      }
      return key;
    });
    (translateServiceMock.get as jest.Mock).mockReturnValue(of('Translated Value'));

    await TestBed.configureTestingModule({
      imports: [
        MemberFormComponent,
        ReactiveFormsModule,
        TranslateModule.forRoot(),
        MatInputModule,
        MatSelectModule,
        MatAutocompleteModule,
        MatFormFieldModule
      ],
      providers: [
        { provide: MemberService,
          useValue: of([memberServiceMock]) },
        { provide: OrganisationUnitService,
          useValue: of([organisationUnitServiceMock]) },
        { provide: Router,
          useValue: routerMock },
        { provide: ActivatedRoute,
          useValue: activatedRouteMock },
        { provide: Router,
          useValue: routerMock },
        { provide: TranslateService,
          useValue: translateServiceMock }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MemberFormComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();

    component.ngOnInit();
  });
});


