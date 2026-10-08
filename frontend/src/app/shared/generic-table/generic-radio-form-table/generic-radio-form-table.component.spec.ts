import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericRadioFormTableComponent } from './generic-radio-form-table.component';
import {
  ExperienceCalculationModel
} from '../../../features/calculations/experience-calculation/experience-calculation.model';
import { GenCol, GenericTableDataSource } from '../generic-table-data-source';
import { experienceCalculation1, experienceCalculation2 } from '../../test/test-data';

describe('GenericRadioFormTableComponent', () => {
  let component: GenericRadioFormTableComponent<ExperienceCalculationModel>;
  let fixture: ComponentFixture<GenericRadioFormTableComponent<ExperienceCalculationModel>>;
  const experienceCalculations = [experienceCalculation1,
    experienceCalculation2];

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
    fixture.componentRef.setInput('data', experienceCalculations);
    fixture.componentRef.setInput('table', getTableData());

    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
