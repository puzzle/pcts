import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationFormComponent } from './calculation-form.component';
import { calculationModel1 } from '../../../shared/test/test-data';
import { provideTranslateService } from '@ngx-translate/core';
import { ExperienceCalculationModel } from '../experience-calculation/experience-calculation.model';
import { Relevancy } from '../relevancy.enum';
import { GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { DegreeCalculationModel } from '../degree-calculation/degree-calculation.dto';

describe('CalculationFormComponent', () => {
  let component: CalculationFormComponent;
  let fixture: ComponentFixture<CalculationFormComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CalculationFormComponent],
      providers: [provideTranslateService()]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationFormComponent);
    fixture.componentRef.setInput('calculation', calculationModel1);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should have correct amount of columns in table', () => {
    expect(component.degreeTable.columnDefs.length)
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
      const [
        nameCol,
        highlyCol,
        limitedCol,
        littleCol
      ] = component.experienceTable.columnDefs;

      expect(nameCol.getValue(mockExperienceCalc))
        .toBe('Test Experience');
      expect(highlyCol.getValue(mockExperienceCalc))
        .toBe(10);
      expect(limitedCol.getValue(mockExperienceCalc))
        .toBe(5);
      expect(littleCol.getValue(mockExperienceCalc))
        .toBe(2);
    });

    const relevancyCases = [
      { relevancy: Relevancy.STRONGLY,
        expected: 10 },
      { relevancy: Relevancy.NORMAL,
        expected: 5 },
      { relevancy: Relevancy.POORLY,
        expected: 2 },
      { relevancy: 'UNKNOWN_RELEVANCY' as any,
        expected: 0 }
    ];

    relevancyCases.forEach(({ relevancy, expected }) => {
      it(`should calculate points correctly for ${relevancy} relevancy`, () => {
        mockExperienceCalc.relevancy = relevancy;
        const pointsCol = component.experienceTable.columnDefs[4];
        expect(pointsCol.getValue(mockExperienceCalc))
          .toEqual(expected);
      });
    });
  });

  describe('Table Data configuration', () => {
    it('should configure GenericTableDataSource with columns', () => {
      expect(component.experienceTable)
        .toBeInstanceOf(GenericTableDataSource);
      expect(component.experienceTable.columnDefs)
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

    it('should correctly map standard properties in degree columns via getValue', () => {
      const [
        nameCol,
        highlyCol,
        limitedCol,
        littleCol
      ] = component.degreeTable.columnDefs;

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

      expect(component.degreeTable.columnDefs[4].getValue(mockDegreeCalc))
        .toEqual('69.00');
    });

    it('should calculate degree points correctly with 100% in one relevancy', () => {
      mockDegreeCalc.relevancies = {
        [Relevancy.STRONGLY]: { weight: 0 },
        [Relevancy.NORMAL]: { weight: 100 },
        [Relevancy.POORLY]: { weight: 0 }
      } as any;

      expect(component.degreeTable.columnDefs[4].getValue(mockDegreeCalc))
        .toEqual('50.00');
    });

    it('should handle missing degree type points gracefully and return 0.00', () => {
      mockDegreeCalc.degree.type = {} as any;
      mockDegreeCalc.relevancies = {
        [Relevancy.STRONGLY]: { weight: 50 },
        [Relevancy.NORMAL]: { weight: 50 },
        [Relevancy.POORLY]: { weight: 0 }
      } as any;

      expect(component.degreeTable.columnDefs[4].getValue(mockDegreeCalc))
        .toEqual('0.00');
    });

    it('should handle missing relevancies gracefully and return 0.00', () => {
      mockDegreeCalc.relevancies = undefined as any;
      expect(component.degreeTable.columnDefs[4].getValue(mockDegreeCalc))
        .toEqual('0.00');
    });
  });

  describe('ReadOnly Behavior', () => {
    const readOnlyCases = [{ isReadOnly: true,
      expectedAriaDisabled: 'true' },
    { isReadOnly: false,
      expectedAriaDisabled: 'false' }];

    readOnlyCases.forEach(({ isReadOnly, expectedAriaDisabled }) => {
      it(`should set the state dropdown disabled to ${expectedAriaDisabled} when isReadOnly is ${isReadOnly}`, async() => {
        fixture.componentRef.setInput('isReadOnly', isReadOnly);
        fixture.detectChanges();
        await fixture.whenStable();

        const selectElement = fixture.nativeElement.querySelector('mat-select');
        expect(selectElement.getAttribute('aria-disabled'))
          .toBe(expectedAriaDisabled);
      });
    });
  });

  describe('Form Validations', () => {
    const createBaseValidCalculation = () => {
      const calc = JSON.parse(JSON.stringify(calculationModel1));

      if (calc.experienceCalculations?.length) {
        calc.experienceCalculations.forEach((exp: any) => {
          exp.relevancy = Relevancy.STRONGLY;
        });
      }

      if (calc.degreeCalculations?.length) {
        calc.degreeCalculations.forEach((deg: any) => {
          deg.relevancies = {
            [Relevancy.STRONGLY]: { checked: true,
              weight: 100 },
            [Relevancy.NORMAL]: { checked: false,
              weight: 0 },
            [Relevancy.POORLY]: { checked: false,
              weight: 0 }
          };
        });
      }
      return calc;
    };

    it('should be VALID when all weights add up to 100 and relevancy is selected', () => {
      const validCalc = createBaseValidCalculation();

      fixture.componentRef.setInput('calculation', validCalc);
      fixture.detectChanges();

      expect(component.calculationForm()
        .valid())
        .toBeTruthy();
    });

    it('should be INVALID when an experience calculation has NO relevancy', () => {
      const invalidCalc = createBaseValidCalculation();

      if (invalidCalc.experienceCalculations.length > 0) {
        invalidCalc.experienceCalculations[0].relevancy = Relevancy.NONE;
      }

      fixture.componentRef.setInput('calculation', invalidCalc);
      fixture.detectChanges();

      expect(component.calculationForm()
        .valid())
        .toBeFalsy();
    });

    it('should be INVALID when degree calculation weights do NOT add up to 100', () => {
      const invalidCalc = createBaseValidCalculation();

      if (invalidCalc.degreeCalculations.length > 0) {
        invalidCalc.degreeCalculations[0].relevancies = {
          [Relevancy.STRONGLY]: { checked: true,
            weight: 50 },
          [Relevancy.NORMAL]: { checked: true,
            weight: 30 },
          [Relevancy.POORLY]: { checked: false,
            weight: 0 }
        };
      }

      fixture.componentRef.setInput('calculation', invalidCalc);
      fixture.detectChanges();

      expect(component.calculationForm()
        .valid())
        .toBeFalsy();
    });
  });
});
