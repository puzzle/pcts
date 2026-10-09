import { Component, effect, input, model, output } from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton } from '@angular/material/radio';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { RadioFormTableResponse } from './radio-form-table-response.model';
import { FormValueControl } from '@angular/forms/signals';
import { experienceCalculation1 } from '../../test/test-data';

@Component({
  imports: [
    ColumnTemplateDirective,
    GenericTableComponent,
    ScopedTranslationPipe,
    MatRadioButton
  ],
  selector: 'app-generic-radio-form-table',
  styleUrl: './generic-radio-form-table.component.scss',
  templateUrl: './generic-radio-form-table.component.html'
})

export class GenericRadioFormTableComponent<T extends object> implements FormValueControl<RadioFormTableResponse<T>[]> {
  protected readonly Relevancy = Relevancy;

  value = model<RadioFormTableResponse<T>[]>([{
    row: experienceCalculation1 as T,
    relevancy: Relevancy.NORMAL
  }]);

  table = input.required<GenericTableDataSource<T>>();

  handleRadioButtonClick = output<{ row: T;
    value: Relevancy; }>();

  constructor() {
    effect(() => {
      const dataValue = this.value()
        .map((entry) => entry.row);
      if (dataValue) {
        this.table().data = dataValue;
      }
    });
  }

  shouldBeChecked(row: { relevancy: Relevancy }, relevancy: Relevancy): boolean {
    return row.relevancy === relevancy;
  }
}


