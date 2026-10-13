import { Component, effect, input, model, output } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { FormValueControl } from '@angular/forms/signals';
import { GenericTableDataSource } from '../generic-table-data-source';
import { CheckBoxTableResponse } from './CheckBoxTableResponse';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';

@Component({
  imports: [
    MatCheckbox,
    GenericTableComponent,
    ScopedTranslationPipe,
    ColumnTemplateDirective
  ],
  selector: 'app-generic-checkbox-form-table',
  styleUrl: './generic-checkbox-form-table.component.scss',
  templateUrl: './generic-checkbox-form-table.component.html'
})
export class GenericCheckboxFormTableComponent<T extends { relevancy: Relevancy }> implements FormValueControl<CheckBoxTableResponse<T>[]> {
  protected readonly Relevancy = Relevancy;

  value = model<CheckBoxTableResponse<T>[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  handleCheckboxClick = output<CheckBoxTableResponse<T>>();

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
