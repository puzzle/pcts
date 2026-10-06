import { Component, effect, input } from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenCol, GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
import { DegreeCalculationModel } from '../../../features/calculations/degree-calculation/degree-calculation.model';

const getTableData = () => new GenericTableDataSource(getColumns())
  .withLimit(10)
  .withDetailViewLink();

const getColumns = (): GenCol<DegreeCalculationModel>[] => [];

@Component({
  imports: [
    ColumnTemplateDirective,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatRadioGroup,
    MatRadioButton
  ],
  selector: 'app-generic-radio-form-table',
  styleUrl: './generic-radio-form-table.component.scss',
  templateUrl: './generic-radio-form-table.component.html'
})

export class GenericRadioFormTableComponent {
  degreeCalculationModel = input.required<DegreeCalculationModel[]>();

  table = getTableData();

  constructor() {
    effect(() => {
      const degreeCalculationModels = this.degreeCalculationModel();
      if (degreeCalculationModels) {
        this.table.data = degreeCalculationModels;
      }
    });
  }
}


