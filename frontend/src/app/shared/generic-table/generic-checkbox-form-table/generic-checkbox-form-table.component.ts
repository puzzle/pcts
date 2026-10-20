import { Component, effect, input, model, output } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import { FormValueControl } from '@angular/forms/signals';
import { GenericTableDataSource } from '../generic-table-data-source';
import { CheckBoxTableResponse } from './CheckBoxTableResponse';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RowDetailTemplateDirective } from '../rowDetailTemplate.directive';
import { TypedTemplateDirective } from '../type-template/typed-template.directive';
import { isBetweenUpperAndLower } from '../../form/form-validators';

@Component({
  imports: [
    MatCheckbox,
    GenericTableComponent,
    ScopedTranslationPipe,
    ColumnTemplateDirective,
    MatInput,
    FormsModule,
    ReactiveFormsModule,
    RowDetailTemplateDirective,
    TypedTemplateDirective,
    MatFormField,
    MatLabel
  ],
  selector: 'app-generic-checkbox-form-table',
  styleUrl: './generic-checkbox-form-table.component.scss',
  templateUrl: './generic-checkbox-form-table.component.html'
})
export class GenericCheckboxFormTableComponent<T extends { id: number;
  relevancies: Map<Relevancy, number>; }> implements FormValueControl<CheckBoxTableResponse<T>[]> {
  protected readonly Relevancy = Relevancy;

  protected readonly Number = Number;

  value = model<CheckBoxTableResponse<T>[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  readonly touch = output<void>();

  highlyRelevant = new FormControl('', isBetweenUpperAndLower(0, 100));

  limitedRelevant = new FormControl('', isBetweenUpperAndLower(0, 100));

  littleRelevant = new FormControl('', isBetweenUpperAndLower(0, 100));

  constructor() {
    effect(() => {
      const value = this.value();

      if (value) {
        this.table().data = value
          .map((entry) => entry.row);
      }
    });
  }

  shouldBeChecked(): boolean {
    return true;
  }
}
