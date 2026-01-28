import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { MemberCalculationTableComponent } from './member-calculation-table.component';
import { MemberService } from '../../member.service';
import { CalculationModel } from '../../../calculations/calculation.model';
import { ActivatedRoute, Router } from '@angular/router';
import Mocked = jest.Mocked;

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
      {
        provide: Router,
        useValue: routerMock
      },
      {
        provide: ActivatedRoute,
        useValue: {}
      }]
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

  it('should call service with provided memberId and roleId', () => {
    fixture.componentRef.setInput('memberId', 42);
    fixture.componentRef.setInput('roleId', 7);
    fixture.detectChanges();

    expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
      .toHaveBeenCalledWith(42, 7);
  });

  it('should set calculations and calculationTable.data', () => {
    const calcData: CalculationModel[] = [{ id: 1,
      points: 1 },
    { id: 2,
      points: 2 }] as CalculationModel[];

    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(calcData));

    fixture.componentRef.setInput('memberId', 1);
    fixture.detectChanges();

    expect(component.calculationTable.data)
      .toEqual(calcData);
  });

  it('should update when roleId changes', () => {
    const newData = [{ id: 99,
      points: 20 } as CalculationModel];
    memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId?.mockReturnValue(of(newData));

    fixture.componentRef.setInput('memberId', 5);
    fixture.detectChanges();

    fixture.componentRef.setInput('roleId', 10);
    fixture.detectChanges();

    expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
      .toHaveBeenCalledWith(5, 10);

    expect(component.calculationTable.data)
      .toEqual(newData);
  });
});
