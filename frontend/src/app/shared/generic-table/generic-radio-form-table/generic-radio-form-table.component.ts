import { Component, effect, input, model, output } from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton } from '@angular/material/radio';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { RadioFormTableResponse } from './radio-form-table-response.model';
import { FormValueControl } from '@angular/forms/signals';

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

export class GenericRadioFormTableComponent<T extends { relevancy: Relevancy }> implements FormValueControl<RadioFormTableResponse<T>[]> {
  protected readonly Relevancy = Relevancy;

  value = model<RadioFormTableResponse<T>[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  handleRadioButtonClick = output<RadioFormTableResponse<T>>();

  constructor() {
    effect(() => {
      const value = this.value();
      if (value) {
        this.table().data = value
          .map((entry) => entry.row);
      }
    });
  }

  shouldBeChecked(row: T, relevancy: Relevancy): boolean {
    return row.relevancy === relevancy;
  }
}


