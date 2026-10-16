import { Component, effect, input, model } from '@angular/core';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { GenericTableDataSource } from '../generic-table-data-source';
import { MatRadioButton } from '@angular/material/radio';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
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

export class GenericRadioFormTableComponent<T extends { relevancy: Relevancy }> implements FormValueControl<{ row: T;
  relevancy: Relevancy; }[]> {
  protected readonly Relevancy = Relevancy;

  value = model<{ row: T;
    relevancy: Relevancy; }[]>([]);

  table = input.required<GenericTableDataSource<T>>();

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


