import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberCalculationTableComponent } from './member-calculation-table.component';
import { MemberService } from '../../member.service';
import { CalculationModel } from '../../../calculations/calculationDto';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { CalculationFormComponent } from '../../../calculations/calculation-form/calculation-form.component';
import { GenericTableComponent } from '../../../../shared/generic-table/generic-table.component';
import { CrudButtonComponent } from '../../../../shared/crud-button/crud-button.component';
import { calculation1, calculationModel1 } from '../../../../shared/test/test-data';

describe('MemberCalculationTableComponent', () => {
  let component: MemberCalculationTableComponent;
  let fixture: ComponentFixture<MemberCalculationTableComponent>;
  let memberServiceMock: Partial<jest.Mocked<MemberService>>;
  let routerMock: Partial<jest.Mocked<Router>>;
  let authServiceMock: Partial<jest.Mocked<AuthService>>;

  beforeEach(async() => {
    memberServiceMock = {
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
        .mockReturnValue(of([]))
    };

    routerMock = {
      navigate: jest.fn()
    };

    authServiceMock = {
      isAdmin: jest.fn()
        .mockReturnValue(true)
    };

    await TestBed.configureTestingModule({
      imports: [MemberCalculationTableComponent],
      providers: [{ provide: MemberService,
        useValue: memberServiceMock },
      { provide: Router,
        useValue: routerMock },
      { provide: AuthService,
        useValue: authServiceMock }]
    })
      .overrideComponent(MemberCalculationTableComponent, {
        remove: { imports: [CalculationFormComponent,
          GenericTableComponent,
          CrudButtonComponent] }
      })
      .compileComponents();

    fixture = TestBed.createComponent(MemberCalculationTableComponent);
    component = fixture.componentInstance;
  });

  describe('Initialization & Logic', () => {
    it('should create', () => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();
      expect(component)
        .toBeTruthy();
    });

    it('should load calculations on init with correct parameters', () => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.componentRef.setInput('roleId', 7);
      fixture.detectChanges();

      expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
        .toHaveBeenCalledWith(42, 7);
    });

    it('should update calculationTable data when service returns data', () => {
      const calcData = [{ id: 1,
        points: 10 }] as CalculationModel[];
      memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(calcData));

      fixture.componentRef.setInput('memberId', 1);
      fixture.detectChanges();

      expect(component.calculationTable().data)
        .toEqual(calcData);
    });

    it('should navigate to calculation detail on openCalculation', () => {
      fixture.componentRef.setInput('memberId', 5);
      fixture.detectChanges();

      component.openCalculation(calculationModel1);

      expect(routerMock.navigate)
        .toHaveBeenCalledWith([
          '/member',
          5,
          'calculation',
          calculation1.id
        ]);
    });

    it('should navigate to new calculation on createCalculation', () => {
      fixture.componentRef.setInput('memberId', 10);
      fixture.componentRef.setInput('roleId', 2);
      fixture.detectChanges();

      component.createCalculation();

      expect(routerMock.navigate)
        .toHaveBeenCalledWith([
          '/member',
          10,
          'calculation',
          'new',
          2
        ]);
    });
  });

  describe('Template Rendering - Admin View', () => {
    beforeEach(() => {
      authServiceMock.isAdmin?.mockReturnValue(true);
    });

    it('should render title and crud-button', () => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();

      const titleEl = fixture.nativeElement.querySelector('h3');
      expect(titleEl.textContent)
        .toContain('MODEL_NAME');

      const crudButton = fixture.nativeElement.querySelector('app-crud-button');
      expect(crudButton)
        .toBeTruthy();
    });

    it('should render generic table and trigger openCalculation on entrySelected', () => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();

      const openCalcSpy = jest.spyOn(component, 'openCalculation');
      const tableDebugElement = fixture.debugElement.query((debugEl) => debugEl.name === 'app-generic-table');

      expect(tableDebugElement)
        .toBeTruthy();

      tableDebugElement.triggerEventHandler('entrySelected', calculation1);
      expect(openCalcSpy)
        .toHaveBeenCalledWith(calculation1);
    });
  });

  describe('Template Rendering - Non-Admin View', () => {
    beforeEach(() => {
      authServiceMock.isAdmin?.mockReturnValue(false);
    });

    it('should render calculation form if data is present and hide admin table', () => {
      const calcData = [{ id: 1,
        points: 1 }] as CalculationModel[];
      memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(calcData));

      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();

      expect(fixture.nativeElement.querySelector('app-generic-table'))
        .toBeFalsy();
      expect(fixture.nativeElement.querySelector('app-crud-button'))
        .toBeFalsy();

      const calcForm = fixture.nativeElement.querySelector('app-calculation-form');
      expect(calcForm)
        .toBeTruthy();
    });

    it('should render fallback hint if no data is present', () => {
      memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of([]));

      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();

      expect(fixture.nativeElement.querySelector('app-calculation-form'))
        .toBeFalsy();
      expect(fixture.nativeElement.querySelector('app-generic-table'))
        .toBeFalsy();

      const htmlContent = fixture.nativeElement.innerHTML;
      expect(htmlContent)
        .toContain('Hint no calculation');
    });
  });
});
