import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationDetailViewComponent } from './calculation-detail-view.component';
import { Observable, of } from 'rxjs';
import { provideTranslateService } from '@ngx-translate/core';
import { Relevancy } from '../relevancy.enum';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.dto';
import { calculationModel1 } from '../../../shared/test/test-data';
import { CalculationService } from '../calculation.service';
import { CalculationModel } from '../calculationDto';

describe('CalculationDetailViewComponent', () => {
  let component: CalculationDetailViewComponent;
  let fixture: ComponentFixture<CalculationDetailViewComponent>;

  let calculationServiceMock: Partial<CalculationService>;

  beforeEach(async() => {
    calculationServiceMock = {
      getCalculationById: jest.fn()
        .mockReturnValue(of(calculationModel1)),
      getTemplate: jest.fn()
        .mockReturnValue(of(Observable<CalculationModel>))
    };

    await TestBed.configureTestingModule({
      imports: [CalculationDetailViewComponent],
      providers: [{
        provide: CalculationService,
        useValue: calculationServiceMock
      },
      provideTranslateService()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationDetailViewComponent);
    fixture.componentRef.setInput('calculation', calculationModel1);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should have correct amount of columns in table', () => {
    const columns = component.getDegreeCalculationColumns();
    expect(columns.length)
      .toEqual(6);
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
      const nameCol = component.experienceTable.columnDefs[0];
      const highlyCol = component.experienceTable.columnDefs[1];
      const limitedCol = component.experienceTable.columnDefs[2];
      const littleCol = component.experienceTable.columnDefs[3];

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
      const pointsCol = component.experienceTable.columnDefs[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(10);
    });

    it('should calculate points correctly for NORMAL relevancy', () => {
      mockExperienceCalc.relevancy = Relevancy.NORMAL;
      const pointsCol = component.experienceTable.columnDefs[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(5);
    });

    it('should calculate points correctly for POORLY relevancy', () => {
      mockExperienceCalc.relevancy = Relevancy.POORLY;
      const pointsCol = component.experienceTable.columnDefs[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(2);
    });

    it('should return 0 points for unknown relevancy', () => {
      mockExperienceCalc.relevancy = 'UNKNOWN_RELEVANCY' as any;
      const pointsCol = component.experienceTable.columnDefs[4];

      expect(pointsCol.getValue(mockExperienceCalc))
        .toEqual(0);
    });
  });

  describe('Table Data configuration', () => {
    it('should configure GenericTableDataSource with columns', () => {
      const tableData = component.experienceTable;

      expect(tableData)
        .toBeInstanceOf(GenericTableDataSource);
      expect(tableData.columnDefs)
        .toEqual(component.experienceTable.columnDefs);
    });
  });

  describe('Degree Table Columns Definition', () => {
    let mockDegreeCalc: DegreeCalculationModel;

    beforeEach(() => {
      mockDegreeCalc = {
        degree: {
          name: 'Master of Science',
          type: {
            highlyRelevantPoints: 100,
            limitedRelevantPoints: 50,
            littleRelevantPoints: 20
          }
        },
        relevancies: {
          [Relevancy.STRONGLY]: { weight: 0 },
          [Relevancy.NORMAL]: { weight: 0 },
          [Relevancy.POORLY]: { weight: 0 }
        }
      } as DegreeCalculationModel;
    });

    it('should have correct amount of columns in degree table', () => {
      const columns = component.getDegreeCalculationColumns();
      expect(columns.length)
        .toEqual(6);
    });

    it('should correctly map standard properties in degree columns via getValue', () => {
      const columns = component.getDegreeCalculationColumns();
      const nameCol = columns[0];
      const highlyCol = columns[1];
      const limitedCol = columns[2];
      const littleCol = columns[3];

      expect(nameCol.getValue(mockDegreeCalc))
        .toBe('Master of Science');
      expect(highlyCol.getValue(mockDegreeCalc))
        .toBeUndefined();
      expect(limitedCol.getValue(mockDegreeCalc))
        .toBeUndefined();
      expect(littleCol.getValue(mockDegreeCalc))
        .toBeUndefined();
    });

    it('should calculate degree points correctly with mixed weights', () => {
      mockDegreeCalc.relevancies = {
        [Relevancy.STRONGLY]: { weight: 50 },
        [Relevancy.NORMAL]: { weight: 30 },
        [Relevancy.POORLY]: { weight: 20 }
      } as any;

      const pointsCol = component.getDegreeCalculationColumns()[4];

      expect(pointsCol.getValue(mockDegreeCalc))
        .toEqual('69.00');
    });

    it('should calculate degree points correctly with 100% in one relevancy', () => {
      mockDegreeCalc.relevancies = {
        [Relevancy.STRONGLY]: { weight: 0 },
        [Relevancy.NORMAL]: { weight: 100 },
        [Relevancy.POORLY]: { weight: 0 }
      } as any;

      const pointsCol = component.getDegreeCalculationColumns()[4];

      expect(pointsCol.getValue(mockDegreeCalc))
        .toEqual('50.00');
    });

    it('should handle missing degree type points gracefully and return 0.00', () => {
      mockDegreeCalc.degree.type = {} as any;
      mockDegreeCalc.relevancies = {
        [Relevancy.STRONGLY]: { weight: 50 },
        [Relevancy.NORMAL]: { weight: 50 },
        [Relevancy.POORLY]: { weight: 0 }
      } as any;

      const pointsCol = component.getDegreeCalculationColumns()[4];

      expect(pointsCol.getValue(mockDegreeCalc))
        .toEqual('0.00');
    });

    it('should handle missing relevancies gracefully and return 0.00', () => {
      mockDegreeCalc.relevancies = undefined as any;

      const pointsCol = component.getDegreeCalculationColumns()[4];

      expect(pointsCol.getValue(mockDegreeCalc))
        .toEqual('0.00');
    });
  });

  describe('Degree Table Data configuration', () => {
    it('should configure GenericTableDataSource with detail view link for degree table', () => {
      const tableData = component.getDegreeCalculationTableData();

      expect(tableData)
        .toBeInstanceOf(GenericTableDataSource);
      expect(tableData.shouldLink)
        .toBe(true);
      expect(tableData.columnDefs.length)
        .toEqual(6);
    });
  });
});
