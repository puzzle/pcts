import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { MemberService } from '../../member/member.service';
import { calculation1 } from '../../../shared/test/test-data';
import { of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { Relevancy } from '../relevancy.enum';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';

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

  it('should call memberService to fetch calculations on init', () => {
    expect(memberServiceMock.getCalculationsByMemberIdAndOptionalRoleId)
      .toHaveBeenCalledWith(1);
  });

  it('should have correct amount of columns in table', () => {
    const columns = component.getColumns;
    expect(columns.length)
      .toEqual(5);
  });

  describe('Table Columns Definition', () => {
    let mockExperienceCalc: ExperienceCalculationModel;

    beforeEach(() => {
      mockExperienceCalc = {
        experience: {
          name: 'Test Experience',
          experienceType: {
            highlyRelevantPoints: 10,
            limitedRelevantPoints: 5,
            littleRelevantPoints: 2
          }
        },
        relevancy: Relevancy.STRONGLY
      } as ExperienceCalculationModel;
    });

    it('should correctly map standard properties in columns via getValue', () => {
      const nameCol = component.getColumns[0];
      const highlyCol = component.getColumns[1];
      const limitedCol = component.getColumns[2];
      const littleCol = component.getColumns[3];

      expect(nameCol.getValue(mockExperienceCalc))
        .toBe('Test Experience');
      expect(highlyCol.getValue(mockExperienceCalc))
        .toBe(10);
      expect(limitedCol.getValue(mockExperienceCalc))
        .toBe(5);
      expect(littleCol.getValue(mockExperienceCalc))
        .toBe(2);
    });

    it('should calculate points correctly for STRONGLY relevancy', () => {
      mockExperienceCalc.relevancy = Relevancy.STRONGLY;
      const pointsCol = component.getColumns[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(10);
    });

    it('should calculate points correctly for NORMAL relevancy', () => {
      mockExperienceCalc.relevancy = Relevancy.NORMAL;
      const pointsCol = component.getColumns[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(5);
    });

    it('should calculate points correctly for POORLY relevancy', () => {
      mockExperienceCalc.relevancy = Relevancy.POORLY;
      const pointsCol = component.getColumns[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(2);
    });

    it('should return 0 points for unknown relevancy', () => {
      mockExperienceCalc.relevancy = 'UNKNOWN_RELEVANCY' as any;
      const pointsCol = component.getColumns[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(0);
    });
  });

  describe('Table Data configuration', () => {
    it('should configure GenericTableDataSource with columns', () => {
      const tableData = component.getTableData();

      expect(tableData)
        .toBeInstanceOf(GenericTableDataSource);
      expect(tableData.columnDefs)
        .toEqual(component.getColumns);
    });

    it('should correctly apply the limit of 10 to the filterPredicateWithIndex', () => {
      const tableData = component.getTableData();

      const testData = {} as ExperienceCalculationModel;

      expect(tableData.filterPredicateWithIndex(testData, '', 9))
        .toBe(true);
      expect(tableData.filterPredicateWithIndex(testData, '', 10))
        .toBe(false);
      expect(tableData.filterPredicateWithIndex(testData, '', 15))
        .toBe(false);
    });
  });
});
