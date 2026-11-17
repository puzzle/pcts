import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { provideTranslateService } from '@ngx-translate/core';
import { calculationModel1 } from '../../../shared/test/test-data';
import { CalculationService } from '../calculation.service';
import { Router } from '@angular/router';
import { CalculationState } from '../calculation-state.enum';
import { DatePipe } from '@angular/common';
import { of } from 'rxjs';
import { CalculationFormComponent } from '../calculation-form/calculation-form.component';

describe('CalculationDetailViewComponent', () => {
  let component: CalculationDetailViewComponent;
  let fixture: ComponentFixture<CalculationDetailViewComponent>;

  let calculationServiceMock: Partial<CalculationService>;
  let routerMock: any;

  beforeEach(async() => {
    calculationServiceMock = {
      updateCalculation: jest.fn()
        .mockReturnValue(of({})),
      addCalculation: jest.fn()
        .mockReturnValue(of({}))
    };

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [CalculationDetailViewComponent],
      providers: [
        { provide: CalculationService,
          useValue: calculationServiceMock },
        { provide: Router,
          useValue: routerMock },
        provideTranslateService(),
        DatePipe
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationDetailViewComponent);
    fixture.componentRef.setInput('calculation', calculationModel1);
    component = fixture.componentInstance;

    fixture.detectChanges();
    await fixture.whenStable();
  });

  describe('Initialization & Basics', () => {
    it('should create', () => {
      expect(component)
        .toBeTruthy();
    });

    it('should identify as "Add" mode if calculation has no ID', () => {
      const newCalc = JSON.parse(JSON.stringify(calculationModel1));
      newCalc.id = null;

      fixture.componentRef.setInput('calculation', newCalc);
      fixture.detectChanges();

      expect(component.isAdd())
        .toBeTruthy();
    });

    it('should identify as "Edit" mode if calculation has an ID', () => {
      expect(component.isAdd())
        .toBeFalsy();
    });
  });

  describe('Routing', () => {
    it('should navigate to member CV when routeToCV is called', () => {
      component.routeToCV();
      expect(routerMock.navigate)
        .toHaveBeenCalledWith(['member',
          calculationModel1.member.id]);
    });
  });

  describe('State (isArchived)', () => {
    it('should return true if calculation state is ARCHIVED', () => {
      const archivedCalc = JSON.parse(JSON.stringify(calculationModel1));
      archivedCalc.state = CalculationState.ARCHIVED;

      fixture.componentRef.setInput('calculation', archivedCalc);
      fixture.detectChanges();

      expect(component.isArchived())
        .toBeTruthy();
    });

    it('should return false if calculation state is NOT ARCHIVED', () => {
      const draftCalc = JSON.parse(JSON.stringify(calculationModel1));
      draftCalc.state = CalculationState.DRAFT;

      fixture.componentRef.setInput('calculation', draftCalc);
      fixture.detectChanges();

      expect(component.isArchived())
        .toBeFalsy();
    });
  });

  describe('Saving Form (saveForm)', () => {
    let mockFormComponent: any;

    beforeEach(() => {
      mockFormComponent = {
        calculationForm: jest.fn()
          .mockReturnValue({
            valid: jest.fn()
              .mockReturnValue(true),
            controlValue: jest.fn()
              .mockReturnValue({ formData: 'test' }),
            markAsTouched: jest.fn()
          })
      };
    });

    it('should update calculation if form is valid and NOT in add mode', () => {
      component.saveForm(mockFormComponent as CalculationFormComponent);

      expect(calculationServiceMock.updateCalculation)
        .toHaveBeenCalledWith(calculationModel1.id, { formData: 'test' });
      expect(routerMock.navigate)
        .toHaveBeenCalledWith(['member',
          calculationModel1.member.id]);
    });

    it('should add calculation if form is valid and in add mode', () => {
      const newCalc = JSON.parse(JSON.stringify(calculationModel1));
      newCalc.id = null;
      fixture.componentRef.setInput('calculation', newCalc);
      fixture.detectChanges();

      component.saveForm(mockFormComponent as CalculationFormComponent);

      expect(calculationServiceMock.addCalculation)
        .toHaveBeenCalledWith({ formData: 'test' });
      expect(routerMock.navigate)
        .toHaveBeenCalledWith(['member',
          calculationModel1.member.id]);
    });

    it('should not call service and mark form as touched if form is invalid', () => {
      mockFormComponent.calculationForm().valid.mockReturnValue(false);

      component.saveForm(mockFormComponent as CalculationFormComponent);

      expect(calculationServiceMock.updateCalculation).not.toHaveBeenCalled();
      expect(calculationServiceMock.addCalculation).not.toHaveBeenCalled();

      expect(mockFormComponent.calculationForm().markAsTouched)
        .toHaveBeenCalled();
    });
  });
});
