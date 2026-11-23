import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { provideTranslateService } from '@ngx-translate/core';
import { calculationModel1 } from '../../../shared/test/test-data';
import { CalculationService } from '../calculation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CalculationState } from '../calculation-state.enum';
import { DatePipe } from '@angular/common';
import { of } from 'rxjs';
import { signal, WritableSignal } from '@angular/core';
import { form, validate } from '@angular/forms/signals';
import { CalculationModel } from '../calculationDto';

describe('CalculationDetailViewComponent', () => {
  let component: CalculationDetailViewComponent;
  let fixture: ComponentFixture<CalculationDetailViewComponent>;

  let calculationServiceMock: Partial<CalculationService>;
  let routerMock: any;
  let routeMock: any;

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

    routeMock = {} as unknown as ActivatedRoute;

    await TestBed.configureTestingModule({
      imports: [CalculationDetailViewComponent],
      providers: [
        { provide: CalculationService,
          useValue: calculationServiceMock },
        { provide: Router,
          useValue: routerMock },
        { provide: ActivatedRoute,
          useValue: routeMock },
        provideTranslateService(),
        DatePipe
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationDetailViewComponent);
    fixture.componentRef.setInput('calculation', calculationModel1);
    fixture.componentRef.setInput('isCreate', true);
    component = fixture.componentInstance;

    fixture.detectChanges();
    await fixture.whenStable();
  });

  describe('Initialization & Basics', () => {
    it('should create', () => {
      expect(component)
        .toBeTruthy();
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
    it('should update calculation if form is valid and is in update mode', async() => {
      const newCalc = JSON.parse(JSON.stringify(calculationModel1));
      fixture.componentRef.setInput('calculation', newCalc);
      fixture.componentRef.setInput('isCreate', false);

      const formModel = signal(calculationModel1);
      const fieldTree = TestBed.runInInjectionContext(() => form(formModel));

      component.save(fieldTree);
      await fixture.whenStable();

      expect(calculationServiceMock.updateCalculation)
        .toHaveBeenCalledWith(calculationModel1.id, formModel());
      expect(routerMock.navigate)
        .toHaveBeenCalledWith(['member',
          calculationModel1.member.id]);
    });

    it('should add calculation if form is valid and in add mode', async() => {
      const newCalc = JSON.parse(JSON.stringify(calculationModel1));
      fixture.componentRef.setInput('calculation', newCalc);
      fixture.detectChanges();

      const formModel: WritableSignal<CalculationModel> = signal(newCalc);
      const fieldTree = TestBed.runInInjectionContext(() => form(formModel));

      component.save(fieldTree);
      await fixture.whenStable();

      expect(calculationServiceMock.addCalculation)
        .toHaveBeenCalledWith(formModel());
      expect(routerMock.navigate)
        .toHaveBeenCalledWith(['member',
          calculationModel1.member.id]);
    });

    it('should not call service if form is invalid', async() => {
      const formModel = signal(calculationModel1);
      const fieldTree = TestBed.runInInjectionContext(() => form(formModel, (schema) => {
        validate(schema.points, () => ({ kind: 'invalid' }));
      }));

      component.save(fieldTree);
      await fixture.whenStable();

      expect(calculationServiceMock.updateCalculation).not.toHaveBeenCalled();
      expect(calculationServiceMock.addCalculation).not.toHaveBeenCalled();
      expect(fieldTree()
        .touched())
        .toBe(true);
    });
  });
});
