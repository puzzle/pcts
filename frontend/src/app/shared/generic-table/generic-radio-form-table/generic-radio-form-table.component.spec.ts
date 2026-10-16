import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericRadioFormTableComponent } from './generic-radio-form-table.component';
import {
  ExperienceCalculationModel
} from '../../../features/calculations/experience-calculation/experience-calculation.model';
import { GenCol, GenericTableDataSource } from '../generic-table-data-source';
import { experienceCalculation1, experienceCalculation2 } from '../../test/test-data';
import { Relevancy } from '../../../features/calculations/relevancy.enum';

describe('GenericRadioFormTableComponent', () => {
  let component: GenericRadioFormTableComponent<ExperienceCalculationModel>;
  let fixture: ComponentFixture<GenericRadioFormTableComponent<ExperienceCalculationModel>>;

  const experienceCalculations = [{ ...experienceCalculation1,
    id: 1,
    relevancy: Relevancy.NORMAL },
  { ...experienceCalculation2,
    id: 2,
    relevancy: Relevancy.POORLY }] as ExperienceCalculationModel[];

  const getTableData = () => new GenericTableDataSource(getColumns())
    .withLimit(10)
    .withDetailViewLink();

  const getColumns = (): GenCol<ExperienceCalculationModel>[] => [
    GenCol.fromCalculated('name', (e: ExperienceCalculationModel) => e.experience.name),
    GenCol.fromCalculated('highlyRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.highlyRelevantPoints),
    GenCol.fromCalculated('limitedRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.limitedRelevantPoints),
    GenCol.fromCalculated('littleRelevant', (e: ExperienceCalculationModel) => e.experience.experienceType.littleRelevantPoints),
    GenCol.fromCalculated('points', () => 0)
  ];

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericRadioFormTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericRadioFormTableComponent);

    fixture.componentRef.setInput('value', experienceCalculations);
    fixture.componentRef.setInput('table', getTableData());

    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  describe('Effect: sync value to table data', () => {
    it('should update table data when value changes', () => {
      fixture.detectChanges();

      expect(component.table().data)
        .toEqual(experienceCalculations);
    });
  });

  describe('shouldBeChecked()', () => {
    it('should return true if row relevancy matches passed relevancy', () => {
      const row = experienceCalculations[0]; // Hat Relevancy.NORMAL

      expect(component.shouldBeChecked(row, Relevancy.NORMAL))
        .toBe(true);
    });

    it('should return false if row relevancy does NOT match passed relevancy', () => {
      const row = experienceCalculations[0]; // Hat Relevancy.NORMAL

      expect(component.shouldBeChecked(row, Relevancy.STRONGLY))
        .toBe(false);
    });
  });

  describe('changeData()', () => {
    it('should update relevancy of specific row and update value model', () => {
      const rowToUpdate = experienceCalculations[0];

      component.changeData(rowToUpdate, Relevancy.STRONGLY);

      const updatedValue = component.value();
      const updatedRow = updatedValue.find((entry) => entry.id === rowToUpdate.id);

      expect(updatedRow)
        .toBeDefined();
      expect(updatedRow?.relevancy)
        .toBe(Relevancy.STRONGLY);

      expect(updatedValue.length)
        .toBe(2);
    });

    it('should emit touch event when data is changed', () => {
      const rowToUpdate = experienceCalculations[1];
      const touchSpy = jest.spyOn(component.touch, 'emit');

      component.changeData(rowToUpdate, Relevancy.NORMAL);

      expect(touchSpy)
        .toHaveBeenCalledTimes(1);
    });
  });
});
