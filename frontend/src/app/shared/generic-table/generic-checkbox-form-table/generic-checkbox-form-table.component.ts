import { Component, computed, effect, input, model, output, Signal, WritableSignal } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { GenericTableComponent } from '../generic-table.component';
import { ScopedTranslationPipe } from '../../pipes/scoped-translation-pipe';
import { Relevancy } from '../../../features/calculations/relevancy.enum';
import {
  applyEach,
  disabled,
  FieldTree,
  form,
  FormField,
  FormValueControl,
  max,
  min,
  ValidationError
} from '@angular/forms/signals';
import { GenericTableDataSource } from '../generic-table-data-source';
import { ColumnTemplateDirective } from '../column-template/column-template.directive';
import { MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RowDetailTemplateDirective } from '../rowDetailTemplate.directive';
import { TypedTemplateDirective } from '../type-template/typed-template.directive';
import { RelevancyWeightsRow } from './RelevancyWeightsRow';
import { DegreeCalculationModel } from '../../../features/calculations/degree-calculation/degree-calculation.dto';
import { MatIcon } from '@angular/material/icon';
import { InvalidTotalError } from '../../../features/calculations/calculation-form/calculation-form.component';

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
    MatLabel,
    FormField,
    MatIcon
  ],
  selector: 'app-generic-checkbox-form-table',
  styleUrl: './generic-checkbox-form-table.component.scss',
  templateUrl: './generic-checkbox-form-table.component.html'
})
export class GenericCheckboxFormTableComponent<T extends RelevancyWeightsRow> implements FormValueControl<T[]> {
  protected readonly Relevancy = Relevancy;

  value = model<T[]>([]);

  table = input.required<GenericTableDataSource<T>>();

  errors = input<readonly ValidationError.WithOptionalFieldTree[]>([]);

  readonly touch = output<void>();

  relevancyForm = new FormGroup({
    comment: new FormControl('')
  });

  constructor() {
    effect(() => {
      const value = this.value();

      if (value) {
        this.table().data = value;
      }
    });
  }

  shouldBeChecked(row: RelevancyWeightsRow, relevancy: Relevancy) {
    for (const entry of this.value()) {
      if (entry === row) {
        return entry.relevancies[relevancy].checked;
      }
    }
    return false;
  }

  updateInputFields(row: RelevancyWeightsRow, relevancy: Relevancy) {
    const indexOfRow = this.rows()
      .value()
      .indexOf(row);
    this.rows[indexOfRow].relevancies().value.update((currentValue) => {
      const target = currentValue[relevancy];

      return { ...currentValue,
        [relevancy]: {
          ...target,
          checked: !target.checked
        } };
    });
  }

  readonly rows: FieldTree<RelevancyWeightsRow[]> = form(this.value as unknown as WritableSignal<DegreeCalculationModel[]>, (rows) => applyEach(rows, (row) => {
    Object.values(Relevancy)
      .forEach((relevancy) => {
        min(row.relevancies[relevancy].weight, 0);
        max(row.relevancies[relevancy].weight, 100);

        disabled(row.relevancies[relevancy].weight, {
          when: ({ valueOf }) => {
            return !valueOf(row.relevancies[relevancy].checked);
          }
        });
      });
  }));

  getRelevancyField(rowId: RelevancyWeightsRow, column: Relevancy) {
    const rows = this.rows;
    for (const entry of rows) {
      if (entry()
        .value() === rowId) {
        return entry.relevancies[column].weight;
      }
    }
    throw new Error('Field does not exist');
  }

  getField(rowId: RelevancyWeightsRow): FieldTree<RelevancyWeightsRow, number, 'writable'> {
    const rows = this.rows;
    for (const entry of rows) {
      if (entry()
        .value() === rowId) {
        return entry;
      }
    }
    throw new Error('Field does not exist');
  }

  private readonly invalidRows: Signal<Set<T>> = computed(() => {
    const errors = this.errors() as unknown as InvalidTotalError<T>[];
    return new Set(errors
      .map((error) => error?.row));
  });

  isRowInvalid(row: T): boolean {
    return this.invalidRows()
      .has(row);
  }
}
