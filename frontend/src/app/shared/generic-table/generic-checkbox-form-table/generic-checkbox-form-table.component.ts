import { Component, effect, input, model, output } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { FormValueControl } from '@angular/forms/signals';
import { GenericTableDataSource } from '../generic-table-data-source';
import { CheckBoxTableResponse } from './CheckBoxTableResponse';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { MatInput } from '@angular/material/input';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  imports: [
    MatCheckbox,
    GenericTableComponent,
    ScopedTranslationPipe,
    ColumnTemplateDirective,
    MatInput,
    FormsModule,
    ReactiveFormsModule
  ],
  selector: 'app-generic-checkbox-form-table',
  styleUrl: './generic-checkbox-form-table.component.scss',
  templateUrl: './generic-checkbox-form-table.component.html'
})
export class GenericCheckboxFormTableComponent<T extends { relevancies: Map<Relevancy, number> }> implements FormValueControl<CheckBoxTableResponse<T>[]> {
  protected readonly Relevancy = Relevancy;

  protected readonly Number = Number;

  value = model<CheckBoxTableResponse<T>[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  handleCheckboxClick = output<CheckBoxTableResponse<T>>();

  highlyRelevant = new FormControl('');

  limitedRelevant = new FormControl('');

  littleRelevant = new FormControl('');

  constructor() {
    effect(() => {
      const value = this.value();

      if (value) {
        this.table().data = value
          .map((entry) => entry.row);
      }
    });
  }

  shouldBeChecked(row: T, methodRelevancy: Relevancy): boolean {
    for (const key of row.relevancies.keys()) {
      return key === methodRelevancy && (row.relevancies.get(key) ?? 0) > 0;
    }
    return false;
  }
}
