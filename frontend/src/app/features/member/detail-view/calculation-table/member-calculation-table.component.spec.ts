import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberCalculationTableComponent } from './member-calculation-table.component';
import { MemberService } from '../../member.service';
import { CalculationModel } from '../../../calculations/calculation.model';
import { ActivatedRoute, Router } from '@angular/router';
import Mocked = jest.Mocked;
import { calculation1 } from '../../../../shared/test/test-data';

describe('MemberCalculationTableComponent (Jest)', () => {
  let component: MemberCalculationTableComponent;
  let fixture: ComponentFixture<MemberCalculationTableComponent>;
  let memberServiceMock: Partial<jest.Mocked<MemberService>>;
  let routerMock: jest.Mocked<Router>;

  beforeEach(async() => {
    memberServiceMock = {
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
    } as Partial<Mocked<MemberService>>;

    routerMock = {
      navigate: jest.fn(),
      url: '/member/1'
    } as any;

    await TestBed.configureTestingModule({
      imports: [MemberCalculationTableComponent],
      providers: [{ provide: MemberService,
        useValue: memberServiceMock },
      { provide: Router,
        useValue: routerMock },
      { provide: ActivatedRoute,
        useValue: {} }]
    })
      .compileComponents();

    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of([]));
    fixture = TestBed.createComponent(MemberCalculationTableComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.componentRef.setInput('memberId', 42);
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('should call service with provided memberId and roleId', async() => {
    fixture.componentRef.setInput('memberId', 42);
    fixture.componentRef.setInput('roleId', 7);
    fixture.detectChanges();

    await fixture.whenStable();

    expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
      .toHaveBeenCalledWith(42, 7);
  });

  it('should set calculations and calculationTable.data', async() => {
    const calcData: CalculationModel[] = [{ id: 1,
      points: 1 },
    { id: 2,
      points: 2 }] as CalculationModel[];

    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(calcData));

    fixture.componentRef.setInput('memberId', 1);
    fixture.detectChanges();

    await fixture.whenStable();

    expect(component.calculationTable().data)
      .toEqual(calcData);
  });

  // Make the test async
  it('should update when roleId changes', async() => {
    const newData = [{ id: 99,
      points: 20 } as CalculationModel];

    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of([]));
    fixture.componentRef.setInput('memberId', 5);
    fixture.detectChanges();
    await fixture.whenStable();

    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(newData));

    fixture.componentRef.setInput('roleId', 10);
    fixture.detectChanges();

    await fixture.whenStable();

    expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
      .toHaveBeenCalledWith(5, 10);
    expect(component.calculationTable().data)
      .toEqual(newData);
  });


  it('should navigate on click', async() => {
    fixture.componentRef.setInput('memberId', 5);
    fixture.detectChanges();

    await fixture.whenStable();
    component.openCalculation(calculation1);

    expect(routerMock.navigate)
      .toHaveBeenCalledWith([
        '/member',
        5,
        'calculation',
        calculation1.id
      ]);
  });

  describe('Template & Bindings', () => {
    it('should render title and add crud-button', async() => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();
      await fixture.whenStable();

      const titleEl = fixture.nativeElement.querySelector('h3');
      expect(titleEl)
        .toBeTruthy();
      // ScopedTranslationPipe übersetzt 'MODEL_NAME', wir erwarten, dass es im Text ist
      expect(titleEl.textContent)
        .toContain('MODEL_NAME');

      const crudButton = fixture.nativeElement.querySelector('app-crud-button');
      expect(crudButton)
        .toBeTruthy();
      expect(crudButton.getAttribute('mode'))
        .toBe('add');
    });

    it('should render generic table with correct inputs', async() => {
      fixture.componentRef.setInput('memberId', 42);
      fixture.detectChanges();
      await fixture.whenStable();

      const tableEl = fixture.nativeElement.querySelector('app-generic-table');

      expect(tableEl)
        .toBeTruthy();
      expect(tableEl.getAttribute('crudBasePath'))
        .toBe('calculation');
      expect(tableEl.getAttribute('idAttr'))
        .toBe('id');
    });

    it('should trigger openCalculation when table emits entrySelected', async() => {
      fixture.componentRef.setInput('memberId', 5);
      fixture.detectChanges();
      await fixture.whenStable();

      const openCalcSpy = jest.spyOn(component, 'openCalculation');

      const tableDebugElement = fixture.debugElement.query((debugEl) => debugEl.name === 'app-generic-table');

      expect(tableDebugElement)
        .toBeTruthy();

      tableDebugElement.triggerEventHandler('entrySelected', calculation1);

      expect(openCalcSpy)
        .toHaveBeenCalledTimes(1);
      expect(openCalcSpy)
        .toHaveBeenCalledWith(calculation1);
    });
  });
});
