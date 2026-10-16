import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { MemberService } from '../../member/member.service';
import { calculation1, experienceCalculation1, experienceCalculation2 } from '../../../shared/test/test-data';
import { of } from 'rxjs';
import { Relevancy } from '../relevancy.enum';
import { provideTranslateService } from '@ngx-translate/core';

describe('CalculationDetailViewComponent', () => {
  let component: CalculationDetailViewComponent;
  let fixture: ComponentFixture<CalculationDetailViewComponent>;

  let memberServiceMock: Partial<MemberService>;

  beforeEach(async() => {
    memberServiceMock = {
      getCalculationsByMemberIdAndOptionalRoleId: jest.fn()
        .mockReturnValue(of([calculation1]))
    };

    await TestBed.configureTestingModule({
      imports: [CalculationDetailViewComponent],
      providers: [{
        provide: MemberService,
        useValue: memberServiceMock
      },
      provideTranslateService()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationDetailViewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should have correct amount of columns in table', () => {
    const columns = component.getColumns;

    expect(columns.length)
      .toEqual(5);
  });

  it('should change table data on method call', () => {
    const tableData = component.experienceCalculationModels.map((entry) => entry.relevancy);

    component.changeData({ row: experienceCalculation1,
      relevancy: Relevancy.NORMAL });

    const changedData = component.experienceCalculationModels.map((entry) => entry.relevancy);

    expect(changedData).not.toEqual(tableData);
  });

  it('should not change table data on method call with argument which is not in table data', () => {
    const tableData = component.experienceCalculationModels.map((entry) => entry.relevancy);

    component.changeData({ row: experienceCalculation2,
      relevancy: Relevancy.NORMAL });

    const changedData = component.experienceCalculationModels.map((entry) => entry.relevancy);

    expect(changedData)
      .toEqual(tableData);
  });
});
