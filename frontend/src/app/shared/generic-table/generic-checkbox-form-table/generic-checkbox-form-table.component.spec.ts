import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericCheckboxFormTableComponent } from './generic-checkbox-form-table.component';
import { degreeCalculation1, degreeCalculation2 } from '../../test/test-data';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { GenCol, GenericTableDataSource } from '../generic-table-data-source';
import { DegreeCalculationModel } from '../../../features/calculations/degree-calculation/degree-calculation.dto';
import { toDegreeCalculationModel } from '../../../features/calculations/degree-calculation/degree-calculation-mapper';

describe('GenericCheckboxFormTableComponent', () => {
  let component: GenericCheckboxFormTableComponent<DegreeCalculationModel>;
  let fixture: ComponentFixture<GenericCheckboxFormTableComponent<DegreeCalculationModel>>;

  const degreeCalculations = [toDegreeCalculationModel(degreeCalculation1),
    toDegreeCalculationModel(degreeCalculation2)] as DegreeCalculationModel[];

  const getTableData = () => new GenericTableDataSource(getColumns())
    .withLimit(10)
    .withDetailViewLink();

  const getColumns = (): GenCol<DegreeCalculationModel>[] => [
    GenCol.fromCalculated('name', (d: DegreeCalculationModel) => d.degree.name),
    GenCol.fromCalculated('highlyRelevant', () => {}),
    GenCol.fromCalculated('limitedRelevant', () => {}),
    GenCol.fromCalculated('littleRelevant', () => {}),
    GenCol.fromCalculated('points', (e: DegreeCalculationModel) => 0),
    GenCol.fromCalculated('errorCol', () => {})
  ];

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCheckboxFormTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericCheckboxFormTableComponent);

    fixture.componentRef.setInput('value', degreeCalculations);
    fixture.componentRef.setInput('table', getTableData());

    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should return true when the row relevancy is checked', () => {
    const row = toDegreeCalculationModel(degreeCalculation1);
    row.relevancies[Relevancy.POORLY].checked = true;

    component.value.set([row]);
    fixture.detectChanges();

    expect(component.shouldBeChecked(row, Relevancy.POORLY))
      .toBeTruthy();
  });

  it('should return false when the row is missing', () => {
    const row = toDegreeCalculationModel(degreeCalculation1);

    component.value.set([]);
    fixture.detectChanges();

    expect(component.shouldBeChecked(row, Relevancy.POORLY))
      .toBeFalsy();
  });

  it('should return false when the row is found but unchecked', () => {
    const row = toDegreeCalculationModel(degreeCalculation1);
    row.relevancies[Relevancy.POORLY].checked = false;

    component.value.set([row]);
    fixture.detectChanges();

    expect(component.shouldBeChecked(row, Relevancy.POORLY))
      .toBeFalsy();
  });

  it('should return the correct weight field for the given row and column', () => {
    const row = toDegreeCalculationModel(degreeCalculation1);

    component.value.set([row]);
    fixture.detectChanges();

    const field = component.getField(row, Relevancy.POORLY);

    expect(field)
      .toBeDefined();
    expect(field()
      .value())
      .toBe(row.relevancies[Relevancy.POORLY].weight);
  });

  it('should throw an error when row is missing', () => {
    const row1 = toDegreeCalculationModel(degreeCalculation1);

    component.value.set([row1]);
    fixture.detectChanges();

    const unknownRow = toDegreeCalculationModel(degreeCalculation2);

    expect(() => component.getField(unknownRow, Relevancy.POORLY))
      .toThrow();
  });
});
